package service;

import model.Transaction;

//Сервис сохранения Транзакций.
public interface TransactionService {
    void save(Transaction transaction);
}
