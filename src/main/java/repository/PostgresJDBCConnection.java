package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresJDBCConnection implements JDBCConnection {
    private static String jdbcURL = "jdbc:postgresql://localhost:5432/hiber";
    private static String jdbcUsername = "postgres";
    private static String jdbcPassword = "qq112233";
    private Connection mainConnection;


    @Override
    public Connection getConnection() throws SQLException {
        if (mainConnection == null ) {
            try {
                mainConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return mainConnection;
        } else {
            return mainConnection;
        }
    }
}
