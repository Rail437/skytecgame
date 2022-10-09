package service;

import manager.QueueManager;
import model.Clan;
import model.Transaction;

public class MainService implements ClanService,  TransactionService {
    private volatile QueueManager queueManager;
    public MainService(QueueManager queueManager){
        this.queueManager = queueManager;
    }

    @Override
    public void save(Transaction transaction) {
        queueManager.save(transaction);
    }

    @Override
    public Clan get(Long clanId) {
        return queueManager.getClanById(clanId);
    }

    @Override
    public Clan save(Clan clan) {
        return queueManager.save(clan);
    }

    @Override
    public void update(Clan clan) {
        queueManager.add(clan);
    }

    @Override
    public Clan getClan(Long clanId) {
        return queueManager.getClanById(clanId);
    }
}
