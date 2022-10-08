package service;

import model.Clan;
import model.Log;
import model.Transaction;
import repository.*;

public class MainService implements ClanService, LogService, TransactionService {
    private final ClanRepository clanRepository;
    private final LogRepository logRepository;
    private final TransactionRepository transactionRepo;

    public MainService(ClanRepository clanRepository, LogRepository logRepository, TransactionRepository transactionRepo) {
        this.clanRepository = clanRepository;
        this.logRepository = logRepository;
        this.transactionRepo = transactionRepo;
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepo.save(transaction);
    }

    @Override
    public void printAllTransactionsByClanId(long id) {
        transactionRepo.printAllTransactionsByClanId(id);
    }

    @Override
    public Clan get(Long clanId) {
        return clanRepository.findById(clanId);
    }

    @Override
    public Clan save(Clan clan) {
        return clanRepository.save(clan);
    }

    @Override
    public Clan getClan(Long clanId) {
        return clanRepository.findById(clanId);
    }

    @Override
    public void removeAllClans() {
        clanRepository.removeAllClans();
    }

    @Override
    public void printClansTable() {
        clanRepository.printClansTable();
    }

    @Override
    public void log(Log log) {
        logRepository.save(log);
    }

    @Override
    public void removeAllLogs() {
        logRepository.clearAllLog();
    }

    @Override
    public void printLogTable() {
        logRepository.printLogTable();
    }

    @Override
    public void printAllLogByClanId(Long clanId) {
        logRepository.printLogsByClanId(clanId);
    }

    public void deleteAllTransactions() {
        transactionRepo.deleteAllTransactions();
    }
}
