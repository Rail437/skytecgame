package service;

import model.Transaction;

public interface TransactionService {
    void save(Transaction transaction);

    void printAllTransactionsByClanId(long id);
}
