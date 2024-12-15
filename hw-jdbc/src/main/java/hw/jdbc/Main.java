package hw.jdbc;

import hw.jdbc.migrator.DbMigrator;
import hw.jdbc.migrator.MigratorOptions;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, NoSuchAlgorithmException {
        MyDataSource.setConfig(new SourceOptions(
                "org.postgresql.Driver",
                "jdbc:postgresql://localhost:5432/",
                "postgres",
                "postgres",
                "test_db"
        ));

        var migrator = new DbMigrator(
                MyDataSource.getInstance(),
                new MigratorOptions(
                        "migration_history",
                        "public",
                        "hw-jdbc\\db",
                        "migration_log.txt"));
        migrator.migrate();
    }
}
