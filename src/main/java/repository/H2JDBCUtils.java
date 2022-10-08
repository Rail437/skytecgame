package repository;

import model.Clan;
import model.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class H2JDBCUtils {
    private AtomicLong atomicLong = new AtomicLong(0);
    private AtomicLong logId = new AtomicLong(0);
//    private static String jdbcURL = "jdbc:h2:~/clan";
//    private static String jdbcUsername = "sa";
//    private static String jdbcPassword = "";
    private static String jdbcURL = "jdbc:postgresql://localhost:5432/hiber";
    private static String jdbcUsername = "postgres";
    private static String jdbcPassword = "qq112233";
    private Connection mainConnection;

    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS clans (id bigserial, name varchar(128),gold numeric)";

    private static final String CREATE_LOG_TABLE_SQL = "CREATE TABLE IF NOT EXISTS logs (id bigserial, date date, text varchar(128))";

    private static final String INSERT_CLANS_SQL = "INSERT INTO clans (id, name, gold) VALUES (?, ?, ?)";

    private static final String INSERT_LOG_SQL = "INSERT INTO logs (id, date, text) VALUES (?, ?, ?)";

    private static final String UPDATE_CLANS_SQL = "UPDATE clans SET name =? , gold =? where id =?";

    private static final String QUERY = "select id,name,gold from clans where id =?";

    private static final String QUERY_LOG = "select id,date,text from logs where id =?";

    private static final String SELECT_ALL_ClANS = "select id,name,gold from clans ";

    private static final String SELECT_ALL_LOGS = "select id, date,text from logs ";

    private static final String REMOVE_ALL_CLANS = "DELETE FROM clans";

    private static final String REMOVE_ALL_LOGS = "DELETE FROM logs";

    public H2JDBCUtils() {
        createTable();
    }

    public Connection getConnection() throws SQLException {
        if (mainConnection == null || mainConnection.isClosed()) {
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

    private void printSQLException(SQLException ex) {
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


    private void createTable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();) {
            statement.execute(CREATE_TABLE_SQL);
//            System.out.println(CREATE_TABLE_SQL);
            statement.execute(CREATE_LOG_TABLE_SQL);
//            System.out.println(CREATE_LOG_TABLE_SQL);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    private void insertRecord(Long id, String name, int gold) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLANS_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, gold);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    private void updateRecord(Long id, String name, int gold) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLANS_SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2,gold);
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public void insertRecordInBatch(List<Clan> clans) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLANS_SQL)) {
            for (Clan clan : clans) {
                if (clan.getId() == null) {
                    clan.setId(atomicLong.incrementAndGet());
                }
                preparedStatement.setLong(1, clan.getId());
                preparedStatement.setString(2, clan.getName());
                preparedStatement.setInt(3, clan.getGold());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public Log insertLog(Log log) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_LOG_SQL)) {
            log.setId(logId.incrementAndGet());
            statement.setLong(1, log.getId());
            statement.setDate(2, log.getDate());
            statement.setString(3, log.getText());
            statement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
        return log;
    }

    public void insertLogInBatch(List<Log> logs) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_LOG_SQL)) {
            for (Log log : logs) {
                log.setId(logId.incrementAndGet());
                statement.setLong(1, logId.incrementAndGet());
                statement.setDate(2, log.getDate());
                statement.setString(3, log.getText());
                statement.addBatch();
            }
            statement.executeBatch();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }


    public Clan save(Clan clan) {
        if (clan.getId() == null) {
            clan.setId(atomicLong.incrementAndGet());
        }
        Clan clanInDB = findById(clan.getId());
        if (clanInDB != null) {
            updateRecord(clan.getId(),clan.getName(),clan.getGold());
        } else {
            insertRecord(clan.getId(), clan.getName(), clan.getGold());
        }
        return clan;
    }


    public Clan findById(Long clanId) {
        Clan clan = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(QUERY);) {
            preparedStatement.setLong(1, clanId);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                AtomicInteger gold = new AtomicInteger(rs.getInt("gold"));
                clan = new Clan(id, name, gold);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return clan;
    }

    public void printClansTable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(SELECT_ALL_ClANS);
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                int gold = rs.getInt("gold");
                System.out.println(id + "," + name + "," + gold);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public List<Clan> findAllClans() {
        List<Clan> clanList = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(SELECT_ALL_ClANS);
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                AtomicInteger gold = new AtomicInteger(rs.getInt("gold"));
                clanList.add(new Clan(id, name, gold));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return clanList;
    }

    public Log findLogById(Long id) {
        Log log = new Log();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(QUERY_LOG);) {
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

    public void removeAllClans() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(REMOVE_ALL_CLANS);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void removeAllLogs() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(REMOVE_ALL_LOGS);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void printLogsTable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
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
}
