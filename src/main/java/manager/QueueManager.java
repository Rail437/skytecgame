package manager;

import model.Clan;
import model.Transaction;
import org.apache.commons.collections4.ListUtils;
import repository.ClanJDBCUtility;
import repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class QueueManager {
    private final ClanJDBCUtility clanJDBCUtility;
    private final TransactionRepository transactionRepository;
    private volatile Map<Long, Clan> clanMap = new ConcurrentHashMap<>();
    private volatile ConcurrentLinkedQueue<Transaction> tList = new ConcurrentLinkedQueue<>();
    private volatile LongAdder lAdder = new LongAdder();
    private volatile LongAdder tAdder = new LongAdder();
    private volatile AtomicInteger flag = new AtomicInteger(0);
    ExecutorService executor = Executors.newFixedThreadPool(5);

    public QueueManager(ClanJDBCUtility jdbcUtility, TransactionRepository transactionRepository) {
        this.clanJDBCUtility = jdbcUtility;
        this.transactionRepository = transactionRepository;
    }

    public synchronized void start() {
        for (int i = 0; i < 4; i++) {
            executor.submit(() -> {
                bufferCheck();
            });
        }
    }

    //В дальнейшем можно оптимизировать и запускать проверку только для нужного клана.
    public void checkClanMap() {
        List<Clan> allClans = clanJDBCUtility.findAllClans();
        for (Clan clan : allClans) {
            clanMap.put(clan.getId(), clan);
        }
    }

    public synchronized void stop() {
        executor.shutdown();
    }

    private synchronized void bufferCheck() {
        while (flag.getAndIncrement() < 3 || tList.size() > 0) { //первая часть приложения может закончить свою работу во время обхода,
            //поэтому необходимо пройтись по остаточным значениям.
//            System.out.println("flag : " + flag.get() + " , " + "tList.size() > 0 : " + (tList.size() > 0));
            List<List<Clan>> partition = ListUtils.partition(new ArrayList(clanMap.values()), 10);
            for (List<Clan> clanList : partition) {
                clanJDBCUtility.saveInBatch(clanList);
            }

            while (tList.size() > 0) {
                List<Transaction> list = tList.stream().limit(1000).collect(Collectors.toList());
                transactionRepository.saveList(list);
                tList.removeAll(list);
            }
        }
    }

    public void removeAllClans() {
        clanMap.clear();
        clanJDBCUtility.removeAllClans();
    }

    public void removeAllTransactions() {
        transactionRepository.deleteAllTransactions();
    }

    public Clan save(Clan clan) {
        if (clan.getId() == null) {
            lAdder.increment();
            clan.setId(lAdder.longValue());
        }
        if (clanMap.get(clan.getId()) == null) {
            clanJDBCUtility.save(clan);
        }
        clanMap.put(clan.getId(), clan);
        return clan;
    }

    public Clan add(Clan clan) {
        return clanMap.put(clan.getId(), clan);
    }

    public Clan getClanById(Long id) {
        return clanMap.get(id);
    }


    public void save(Transaction transaction) {
        tAdder.increment();
        transaction.setId(tAdder.longValue());
        tList.add(transaction);
//        transactionRepository.save(transaction);
//        System.out.println(transaction);
    }

    public List<Clan> getClanList() {
        return new ArrayList<>(clanMap.values());
    }
}
