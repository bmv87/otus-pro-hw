package ru.otus.pro.hw.jdbc;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class MyDataSource {

    @Getter
    public static class SourceOptopns {
        private final String connectionString;
        private final String dbDriver;

        public SourceOptopns(String connectionString, String dbDriver) {
            this.connectionString = connectionString;//"jdbc:postgresql://localhost:5432/pattern_jdbc?user=postgres&password=postgres";
            this.dbDriver = dbDriver;//"org.postgresql.Driver"
        }
    }

    private static SourceOptopns options;

    public static void setConfig(SourceOptopns config) {
        if (options == null) {
            options = config;
        } else {
            throw new DBSourceConfigurationException("Connection parameters cannot be changed at runtime.");
        }
    }

    private static volatile MyDataSource instance;

    private MyDataSource() throws ClassNotFoundException {
        if(options.dbDriver == null){
            throw new DBSourceConfigurationException("options.dbDriver", "null");
        }
        if(options.connectionString == null){
            throw new DBSourceConfigurationException("options.connectionString", "null");
        }
        Class.forName(options.dbDriver);
    }

    public static MyDataSource getInstance() throws ClassNotFoundException {

        MyDataSource localInstance = instance;
        if (localInstance == null) {
            synchronized (MyDataSource.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MyDataSource();
                }
            }
        }
        return localInstance;
    }


    public Connection getConnection() throws SQLException {
        //todo: где-то тут должен быть пул подключений
        return DriverManager
                .getConnection(options.connectionString);
    }

}
