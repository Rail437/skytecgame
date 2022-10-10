package repository;

import model.Clan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class ClanJDBCUtility extends JDBCUtils {
    private JDBCConnection jdbcConnection;
    private volatile LongAdder longAdder = new LongAdder();

    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS clans (id bigserial, name varchar(128),gold numeric)";
    private static final String INSERT_CLANS_SQL = "INSERT INTO clans (id, name, gold) VALUES (?, ?, ?)";
    private static final String UPDATE_CLANS_SQL = "UPDATE clans SET name =? , gold =? where id =?";
    private static final String QUERY = "select id,name,gold from clans where id =?";
    private static final String SELECT_ALL_ClANS = "select id,name,gold from clans ";
    private static final String REMOVE_ALL_CLANS = "DELETE FROM clans";

    public ClanJDBCUtility(JDBCConnection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;
        createTable();
    }

    private void createTable() {
        try {
            baseExecute(jdbcConnection.getConnection(), CREATE_TABLE_SQL);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    private void insertRecord(Long id, String name, int gold) {
        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLANS_SQL);
            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, gold);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }


    public synchronized void updateRecord(Long id, String name, int gold) {
        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLANS_SQL);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, gold);
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public synchronized void update(Clan clan) {
        updateRecord(clan.getId(), clan.getName(), clan.getGold());
    }

    public synchronized Clan save(Clan clan) {
        insertRecord(clan.getId(), clan.getName(), clan.getGold());
        return clan;
    }

    public synchronized void saveInBatch(List<Clan> clans) {
        if (!clans.isEmpty()) {
            try {
                Connection connection = jdbcConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLANS_SQL);
                for (Clan clan : clans) {
                    preparedStatement.setString(1, clan.getName());
                    preparedStatement.setInt(2, clan.getGold());
                    preparedStatement.setLong(3, clan.getId());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }
    }


    public Clan findById(Long clanId) {
        Clan clan = null;
        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);
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


    public List<Clan> findAllClans() {
        List<Clan> clanList = new ArrayList<>();
        try (Connection connection = jdbcConnection.getConnection();
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


    public void printClansTable() {
        try {
            Connection connection = jdbcConnection.getConnection();
            Statement statement = connection.createStatement();
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

    public void removeAllClans() {
        try {
            baseExecute(jdbcConnection.getConnection(), REMOVE_ALL_CLANS);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
}
