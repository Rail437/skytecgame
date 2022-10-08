package repository;

import java.sql.Connection;
import java.sql.SQLException;

public interface JDBCUtility {
    Connection getConnection()throws SQLException;
}
