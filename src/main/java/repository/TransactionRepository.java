package repository;

import model.Transaction;

public interface TransactionRepository {
    void save(Transaction transaction);
    void deleteAllTransactions();

    void printAllTransactionsByClanId(long id);
}
