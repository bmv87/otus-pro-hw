package hw.jdbc;

import lombok.Getter;

import java.sql.Connection;
import java.sql.SQLException;

public final class MyDataSource {


    @Getter
    private static SourceOptions options;

    public static void setConfig(SourceOptions config) {
        if (options == null) {
            options = config;
        } else {
            throw new DBSourceConfigurationException("Connection parameters cannot be changed at runtime.");
        }
    }

    private static volatile MyDataSource instance;
    private static volatile ConnectionPool connectionPool;

    private MyDataSource(ConnectionPool pool) {
        connectionPool = pool;
        if (options.getDbDriver() == null) {
            throw new DBSourceConfigurationException("options.dbDriver", "null");
        }
        if (options.getUser() == null) {
            throw new DBSourceConfigurationException("options.user", "null");
        }
        if (options.getPassword() == null) {
            throw new DBSourceConfigurationException("options.password", "null");
        }
        try {
            Class.forName(options.getDbDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static MyDataSource getInstance() {

        MyDataSource localInstance = instance;
        if (localInstance == null) {
            synchronized (MyDataSource.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MyDataSource(new ConnectionPool(options, 10));
                }
            }
        }
        return localInstance;
    }


    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    public void close() {
        connectionPool.shutdown();
    }
}
