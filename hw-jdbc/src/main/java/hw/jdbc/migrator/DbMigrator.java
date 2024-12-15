package hw.jdbc.migrator;

import hw.jdbc.MyDataSource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbMigrator {
    private MyDataSource dataSource;
    private MigratorOptions options;
    private static Pattern COMMENT_PATTERN = Pattern.compile("–.*|/\\*(.|[\\r\\n])*?\\*/");
    // private static String EXIST_DB_REQUEST = "SELECT 1 AS result FROM
    // pg_catalog.pg_database WHERE datname = ?;";
    private static String CHECK_DB_REQUEST = "SELECT datname FROM pg_catalog.pg_database WHERE datname = ?;";
    private static String CHECK_MIGRATION_REQUEST = "SELECT title, checksum FROM <table> ORDER BY id;";
    private static String CREATE_DB_REQUEST = "CREATE DATABASE <db>;";
    private static String GRANT_DB_REQUEST = "GRANT ALL PRIVILEGES ON DATABASE \"<db>\" to <user>;";
    private static String CREATE_SCHEMA_REQUEST = "CREATE SCHEMA IF NOT EXISTS <schema>;";
    private static String CREATE_TABLE_REQUEST = "CREATE TABLE IF NOT EXISTS <table> (\n" +
            "    id SERIAL PRIMARY KEY,\n" +
            "    title VARCHAR(500),\n" +
            "    checksum VARCHAR\n" +
            ");";
    private static String INSERT_LOG_REQUEST = "INSERT INTO <table>(title, checksum) VALUES ('<title>','<checksum>');";

    @Getter
    private static class ScriptInfo {
        private final String fileName;
        private final String checksum;
        @Setter
        private boolean migrationExists;
        private List<String> scripts;

        public ScriptInfo(String fileName, String checksum, List<String> scripts) {
            this.fileName = fileName;
            this.checksum = checksum;
            this.scripts = scripts;
        }

    }

    public DbMigrator(MyDataSource dataSource, MigratorOptions options) {
        this.dataSource = dataSource;
        this.options = options;
    }
  
    private Connection connectToPostgresDB() throws SQLException {
        var connOptions = MyDataSource.getOptions();
        Properties parameters = new Properties();

        if (connOptions.getUser() != null) {
            parameters.put("user", connOptions.getUser());
        }
        if (connOptions.getPassword() != null) {
            parameters.put("password", connOptions.getPassword());
        }
        // if (connOptions.getDatabase() != null) {
        //     parameters.put("database", "postgres");
        // }
        return DriverManager.getConnection(connOptions.getUrl(), parameters);
    }

    private void createDatabaseIfNotExists() throws SQLException {
        var connOptions = MyDataSource.getOptions();
        try (var conn = connectToPostgresDB()) {
            System.out.println("connected to postgres db");
            var isDbExists = false;
            try (PreparedStatement statement = conn.prepareStatement(CHECK_DB_REQUEST)) {
                statement.setString(1, connOptions.getDatabase());
                var result = statement.executeQuery();
                isDbExists = result.next();
            }
            if (!isDbExists) {
                try (Statement statement = conn.createStatement();) {
                    statement.executeUpdate(CREATE_DB_REQUEST.replaceAll("<db>", connOptions.getDatabase()));
                }
                try (Statement statement = conn.createStatement();) {
                    statement.executeUpdate(GRANT_DB_REQUEST.replaceAll("<db>", connOptions.getDatabase())
                            .replaceAll("<user>", connOptions.getUser()));
                }
            }
        }
    }

    public void prepareDatabase() throws SQLException {
        createDatabaseIfNotExists();

        try (var conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try (Statement statement = conn.createStatement();) {
                System.out.println("connected to db: " + conn.getCatalog());

                statement.addBatch(CREATE_SCHEMA_REQUEST.replaceAll("<schema>", options.getShema()));
                statement.addBatch(CREATE_TABLE_REQUEST.replaceAll("<table>", options.getHistoryTableName()));
                statement.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void migrate() throws SQLException, IOException, NoSuchAlgorithmException {
        prepareDatabase();
        List<String> scriptFilesPaths = parseMigrationLog(options.getMigrationLogDirectory(),
                options.getMigrationLogFile());
        List<ScriptInfo> migrationScripts = new ArrayList<>();
        for (var scriptFile : scriptFilesPaths) {
            ScriptInfo info = parseSQLScript(scriptFile);
            migrationScripts.add(info);

        }

        if (migrationScripts.isEmpty() || migrationScripts.stream().allMatch(ms -> ms.getScripts().isEmpty())) {
            return;
        }
        var addedBefore = migrationScripts.stream().filter(m -> m.isMigrationExists()).count();

        try (var conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            checkMigrations(conn, migrationScripts);
            for (var info : migrationScripts) {
                if (info.isMigrationExists()) {
                    System.out.println("Migration alredy exists " + info.getFileName());
                    continue;
                }
                System.out.println("Migration start " + info.getFileName());
                info.getScripts().add(INSERT_LOG_REQUEST.replace("<table>", options.getHistoryTableName())
                        .replace("<title>", info.getFileName().toLowerCase())
                        .replace("<checksum>", info.getChecksum()));
                try {
                    executeSQLBatches(conn, info.getScripts(), 10);
                    info.setMigrationExists(true);
                    System.out.println("Success " + info.getFileName());
                } catch (Exception e) {
                    System.out.println("Error " + info.getFileName());
                    e.getStackTrace();
                    break;
                }
            }
        }
        var totalCount = migrationScripts.size();
        var addedAfter = migrationScripts.stream().filter(m -> m.isMigrationExists()).count();
        System.out.println("Total migrations " + totalCount);
        System.out.println("Success migrations " + (addedAfter - addedBefore));
    }

    private void checkMigrations(Connection connection, List<ScriptInfo> migrations) throws SQLException {
        try (Statement statement = connection
                .createStatement()) {
            var result = statement
                    .executeQuery(CHECK_MIGRATION_REQUEST.replace("<table>", options.getHistoryTableName()));
            var index = 0;
            while (result.next()) {

                var title = result.getString("title");
                var checksum = result.getString("checksum");

                if ((index + 1) > migrations.size()) {
                    throw new MigratorException("В базе найдена миграция, которой нет среди списка миграций в файле "
                            + options.getMigrationLogFile());
                }
                var info = migrations.get(index);

                if (!info.getFileName().equals(title)) {
                    throw new MigratorException("Исходная последовательность миграций изменена в файле "
                            + options.getMigrationLogFile());
                }

                if (!info.getChecksum().equals(checksum)) {
                    throw new MigratorException("Файл миграции был изменен.");
                }
                info.setMigrationExists(true);
            }
        }
    }

    private void executeSQLBatches(Connection connection, List<String> sqlStatements, int batchSize)
            throws SQLException {
        int count = 0;
        try (Statement statement = connection.createStatement();) {

            for (String sql : sqlStatements) {
                statement.addBatch(sql);
                count++;

                if (count % batchSize == 0) {
                    System.out.println("Executing batch");
                    statement.executeBatch();
                    statement.clearBatch();
                }
            }
            if (count % batchSize != 0) {
                System.out.println("Executing batch");
                statement.executeBatch();
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
    }

    private ScriptInfo parseSQLScript(String scriptFilePath) throws IOException, NoSuchAlgorithmException {
        List<String> sqlStatements = new ArrayList<>();
        byte[] data = Files.readAllBytes(Paths.get(scriptFilePath));
        byte[] hash = MessageDigest.getInstance("MD5").digest(data);
        String checksum = new BigInteger(1, hash).toString(16);
        try (BufferedReader reader = new BufferedReader(new FileReader(scriptFilePath))) {
            StringBuilder currentStatement = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher commentMatcher = COMMENT_PATTERN.matcher(line);
                line = commentMatcher.replaceAll("");

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                currentStatement.append(line).append(" ");

                if (line.endsWith(";")) {
                    sqlStatements.add(currentStatement.toString());
                    System.out.println(currentStatement.toString());
                    currentStatement.setLength(0);
                }
            }
        } catch (IOException e) {
            throw e;
        }
        var paths = scriptFilePath.toLowerCase().split("\\\\");
        return new ScriptInfo(paths[paths.length - 1], checksum, sqlStatements);
    }

    static List<String> parseMigrationLog(String fileDir, String fileName) throws IOException {
        List<String> sqlStatements = new ArrayList<>();
        var filePath = Paths.get(fileDir, fileName).toFile();
        System.out.println(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }
                var scriptFile = Paths.get(fileDir, line).toFile().toString();
                sqlStatements.add(scriptFile);
                System.out.println(scriptFile);
            }
        } catch (IOException e) {
            throw e;
        }
        return sqlStatements;
    }
}
