package repository;

import model.Log;

import java.sql.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class JDBCUtils {

    public void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }


    public void baseExecute(Connection connection, String SQL) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(SQL);
    }


}
