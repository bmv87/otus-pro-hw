package hw.jdbc;

import lombok.Getter;
import lombok.Setter;

@Getter
public class SourceOptions {
    private final String dbDriver;
    private final String url;
    @Setter
    private String database;
    private final String user;
    private final String password;

    public SourceOptions(String dbDriver, String url, String user, String password) {
        this.dbDriver = dbDriver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public SourceOptions(String dbDriver, String url, String user, String password, String database) {
        this(dbDriver, url, user, password);
        this.database = database;
    }

}
