package repository;

import model.Transaction;

import java.util.List;

public interface TransactionRepository {
    void save(Transaction transaction);
    void deleteAllTransactions();
    void printAllTransactionsByClanId(long id);
    void saveList(List<Transaction> transactionList);
}
