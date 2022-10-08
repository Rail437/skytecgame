package repository;

import model.Clan;
import model.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class RepositoryImpl implements ClanRepository, LogRepository {
    private final ClanJDBCUtility clanJDBCUtil;
    private final LogJDBCUtility logJDBCUtility;
    private volatile LongAdder longAdderClan = new LongAdder();
    private volatile LongAdder longAdderLog = new LongAdder();
    private volatile LinkedBlockingDeque<Clan> clanList = new LinkedBlockingDeque<Clan>();
    private volatile LinkedBlockingDeque<Log> logList = new LinkedBlockingDeque<Log>();
    private volatile ExecutorService executor = Executors.newSingleThreadExecutor();


    public RepositoryImpl(JDBCUtility jdbcUtility) {
        this.clanJDBCUtil = new ClanJDBCUtility(jdbcUtility);
        this.logJDBCUtility = new LogJDBCUtility(jdbcUtility);
    }

    @Override
    public synchronized Clan save(Clan clan) {
        if (clan.getId() == null) {
            longAdderClan.increment();
            clan.setId(longAdderClan.longValue());
        }
        if (clanList.add(clan)) {
            checkClanList();
        }
        return clan;
    }

    private synchronized void checkClanList() {
        while (clanList.size() > 10) {
            List<Clan> clans = clanList.stream().limit(10).collect(Collectors.toList());
            clanJDBCUtil.saveInBatch(clans);
            clanList.removeAll(clans);
        }
        if (clanList.size() <= 10) {
            for (int i = 0; i < 10; i++) {
                clanJDBCUtil.save(clanList.getFirst());
            }
        }
    }

    @Override
    public Clan findById(Long id) {
        return clanJDBCUtil.findById(id);
    }

    @Override
    public List<Clan> findAllClans() {
        return clanJDBCUtil.findAllClans();
    }

    public void printClansTable() {
        clanJDBCUtil.printClansTable();
    }

    public void removeAllClans() {
        clanJDBCUtil.removeAllClans();
    }


    @Override
    public synchronized Log save(Log log) {
        longAdderLog.increment();
        log.setId(longAdderClan.longValue());
        if (logList.add(log)) {
         checkLogList();
        }
        return log;
    }

    private synchronized void checkLogList() {
        while (logList.size() > 10) {
            List<Log> logs = logList.stream().limit(10).collect(Collectors.toList());
            logJDBCUtility.insertLogInBatch(logs);
            logList.removeAll(logs);
        }
        if (logList.size() <= 10) {
            for (int i = 0; i < 10; i++) {
                Log log = logJDBCUtility.insertLog(logList.getFirst());
                logList.remove(log);
            }
        }
    }

    @Override
    public Log findByid(Long id) {
        return logJDBCUtility.findLogById(id);
    }

    public void printLogTable() {
        logJDBCUtility.printLogsTable();
    }

    @Override
    public void clearAllLog() {
        logJDBCUtility.removeAllLogs();
    }

    @Override
    public void printLogsByClanId(Long clanId) {
        logJDBCUtility.printLogsByClanId(clanId);
    }

    public void removeAllLogs() {
        logJDBCUtility.removeAllLogs();
    }
}
