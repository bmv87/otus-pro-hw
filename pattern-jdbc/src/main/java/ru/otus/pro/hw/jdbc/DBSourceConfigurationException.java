package ru.otus.pro.hw.jdbc;

public class DBSourceConfigurationException extends RuntimeException {
    public DBSourceConfigurationException(String configName, String configValue) {
        super(String.format("DB Source Config %s has wrong value %s", configName, configValue));
    }
    public DBSourceConfigurationException(String error) {
        super(error);
    }
}
