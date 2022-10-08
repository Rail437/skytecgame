package repository;

import model.Transaction;

import java.sql.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;


public class TransactionRepo extends JDBCUtils implements TransactionRepository {
    private final JDBCUtility jdbcUtility;
    private volatile LongAdder longAdder = new LongAdder();
    private volatile LinkedBlockingDeque<Transaction> transactionsDequeue = new LinkedBlockingDeque<Transaction>();
    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Transactions (id bigserial, clanId bigserial, date date, baseGold integer, operationGold integer, status boolean)";
    private static final String DELETE_SQL = "DELETE FROM Transactions";
    private static final String INSERT_SQL = "INSERT INTO Transactions (id, clanId, date, baseGold, operationGold, status) values (?,?,?,?,?,?)";
    private static final String SELECT_SQL = "SELECT * from Transactions where clanId =?";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TransactionRepo(JDBCUtility jdbcUtility) {
        this.jdbcUtility = jdbcUtility;
        createTable();
    }

    private void createTable() {
        try {
            baseExecute(jdbcUtility.getConnection(), CREATE_TABLE_SQL);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }


    @Override
    public synchronized void save(Transaction transaction) {
        longAdder.increment();
        transaction.setId(longAdder.longValue());
        if (transactionsDequeue.add(transaction)) {
            checkDeque();
        }
    }

    private void checkDeque() {
        try {
            Connection connection = jdbcUtility.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
            while (transactionsDequeue.size() > 10) {
                List<Transaction> transactionList = transactionsDequeue.stream().limit(10).collect(Collectors.toList());
                for (Transaction transaction : transactionList) {
                    statement.setLong(1, transaction.getId());
                    statement.setLong(2, transaction.getClanId());
                    statement.setDate(3, transaction.getDate());
                    statement.setInt(4, transaction.getBaseGold());
                    statement.setInt(5, transaction.getOperationGold());
                    statement.setBoolean(6, false);
                    statement.addBatch();
                }
                statement.executeBatch();
                transactionsDequeue.removeAll(transactionList);
            }
            for (Transaction transaction : transactionsDequeue) {
                statement.setLong(1, transaction.getId());
                statement.setLong(2, transaction.getClanId());
                statement.setDate(3, transaction.getDate());
                statement.setInt(4, transaction.getBaseGold());
                statement.setInt(5, transaction.getOperationGold());
                statement.setBoolean(6, false);
                statement.executeUpdate();
                transactionsDequeue.remove(transaction);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }


    @Override
    public void deleteAllTransactions() {
        try {
            baseExecute(jdbcUtility.getConnection(), DELETE_SQL);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    @Override
    public void printAllTransactionsByClanId(long clid) {
        try {
            Connection connection = jdbcUtility.getConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_SQL);
            statement.setLong(1, clid);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                long clanId = resultSet.getLong("clanId");
                Date date = resultSet.getDate("date");
                int baseGold = resultSet.getInt("baseGold");
                int operationGold = resultSet.getInt("operationGold");
                boolean status = resultSet.getBoolean("status");
                System.out.println(new Transaction(
                        id, clanId, date, baseGold, operationGold, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
