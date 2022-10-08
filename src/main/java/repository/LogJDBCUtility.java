package repository;

import model.Log;

import java.sql.*;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

public class LogJDBCUtility extends JDBCUtils {
    private final JDBCUtility jdbcUtility;
    private volatile LongAdder longAdder = new LongAdder();

    private static final String CREATE_LOG_TABLE_SQL = "CREATE TABLE IF NOT EXISTS logs (id bigserial, date date, text varchar(128))";
    private static final String INSERT_LOG_SQL = "INSERT INTO logs (id, date, text) VALUES (?, ?, ?)";
    private static final String QUERY_LOG = "select id,date,text from logs where id =?";
    private static final String SELECT_ALL_LOGS = "select id, date,text from logs ";
    private static final String REMOVE_ALL_LOGS = "DELETE FROM logs";
    private static final String SELECT_ALL_LOGS_BY_CLANID = "select id, date,text from logs where text like ?";


    public LogJDBCUtility(JDBCUtility jdbcUtility) {
        this.jdbcUtility = jdbcUtility;
        createTable();
    }

    private void createTable() {
        try {
            baseExecute(jdbcUtility.getConnection(), CREATE_LOG_TABLE_SQL);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }


    public synchronized Log insertLog(Log log) {
        try {
            Connection connection = jdbcUtility.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_LOG_SQL);
            longAdder.increment();
            log.setId(longAdder.longValue());
            statement.setLong(1, log.getId());
            statement.setDate(2, log.getDate());
            statement.setString(3, log.getText());
            statement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
        return log;
    }

    public synchronized void insertLogInBatch(List<Log> logs) {
        try {
            Connection connection = jdbcUtility.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_LOG_SQL);
            for (Log log : logs) {
                longAdder.increment();
                log.setId(longAdder.longValue());
                statement.setLong(1, log.getId());
                statement.setDate(2, log.getDate());
                statement.setString(3, log.getText());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }


    public Log findLogById(Long id) {
        Log log = new Log();
        try {
            Connection connection = jdbcUtility.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LOG_SQL);
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                log.setId(rs.getLong("id"));
                log.setDate(rs.getDate("date"));
                log.setText(rs.getString("text"));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return log;
    }

    public void removeAllLogs() {
        try {
            baseExecute(jdbcUtility.getConnection(), REMOVE_ALL_LOGS);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void printLogsTable() {
        try {
            Connection connection = jdbcUtility.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(SELECT_ALL_LOGS);
            while (rs.next()) {
                long id = rs.getLong("id");
                Date date = rs.getDate("date");
                String text = rs.getString("text");
                System.out.println(id + "," + date + "," + text);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void printLogsByClanId(Long clanId) {
        Log log = new Log();
        try {
            Connection connection = jdbcUtility.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_LOGS_BY_CLANID);
            preparedStatement.setString(1, "'Clan "+clanId+"%'");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                log.setId(rs.getLong("id"));
                log.setDate(rs.getDate("date"));
                log.setText(rs.getString("text"));
                System.out.println(log);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
}
