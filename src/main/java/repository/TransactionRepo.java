package repository;

import model.Transaction;

import java.sql.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TransactionRepo extends JDBCUtils implements TransactionRepository {
    private final JDBCConnection jdbcConnection;
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Transactions (id bigserial, clanId bigserial, date date, baseGold integer, operationGold integer, text varchar(256), status boolean)";
    private static final String DELETE_SQL = "DELETE FROM Transactions";
    private static final String INSERT_SQL = "INSERT INTO Transactions (id, clanId, date, baseGold, operationGold,text , status) values (?,?,?,?,?,?,?)";
    private static final String SELECT_SQL = "SELECT * from Transactions where clanId =?";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TransactionRepo(JDBCConnection jdbcConnection) {
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


    @Override
    public void save(Transaction transaction) {
        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
            statement.setLong(1, transaction.getId());
            statement.setLong(2, transaction.getClanId());
            statement.setDate(3, transaction.getDate());
            statement.setInt(4, transaction.getBaseGold());
            statement.setInt(5, transaction.getOperationGold());
            statement.setString(6,transaction.getText());
            statement.setBoolean(7, false);
            statement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public void saveList(List<Transaction> transactionList) {
        if (!transactionList.isEmpty()) {
            try {
                Connection connection = jdbcConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
                for (Transaction transaction : transactionList) {
                    statement.setLong(1, transaction.getId());
                    statement.setLong(2, transaction.getClanId());
                    statement.setDate(3, transaction.getDate());
                    statement.setInt(4, transaction.getBaseGold());
                    statement.setInt(5, transaction.getOperationGold());
                    statement.setString(6,transaction.getText());
                    statement.setBoolean(7, false);
                    statement.addBatch();
                }
                statement.executeBatch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void deleteAllTransactions() {
        try {
            baseExecute(jdbcConnection.getConnection(), DELETE_SQL);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public void printAllTransactionsByClanId(long clid) {
        try {
            Connection connection = jdbcConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_SQL);
            statement.setLong(1, clid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long clanId = resultSet.getLong("clanId");
                Date date = resultSet.getDate("date");
                int baseGold = resultSet.getInt("baseGold");
                int operationGold = resultSet.getInt("operationGold");
                String text = resultSet.getString("text");
                boolean status = resultSet.getBoolean("status");
                System.out.println(new Transaction(
                        id, clanId, date, baseGold, operationGold, text, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
