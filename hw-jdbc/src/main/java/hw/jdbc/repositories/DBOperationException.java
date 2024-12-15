package hw.jdbc.repositories;

import java.sql.SQLException;

public class DBOperationException extends RuntimeException{

    public DBOperationException(SQLException e){
        super(e.getMessage(), e);
    }
}
