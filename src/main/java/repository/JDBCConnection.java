package repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface JDBCConnection {
    Connection getConnection()throws SQLException;
}
