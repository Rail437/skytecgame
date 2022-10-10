package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2JDBCConnection implements JDBCConnection {
    private static String jdbcURL = "jdbc:h2:~/clan";
    private static String jdbcUsername = "sa";
    private static String jdbcPassword = "";
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
