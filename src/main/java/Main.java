import controller.ClanController;
import manager.QueueManager;
import model.Clan;
import repository.ClanJDBCUtility;
import repository.JDBCConnection;
import repository.PostgresJDBCConnection;
import repository.TransactionRepo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        JDBCConnection jdbcConnection = new PostgresJDBCConnection();
        QueueManager manager = new QueueManager(new ClanJDBCUtility(jdbcConnection) ,new TransactionRepo(jdbcConnection));
        ClanController controller = new ClanController(manager);
        ExecutorService executorService = Executors.newFixedThreadPool(65);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        manager.checkClanMap();
        executor.submit(()->manager.start());
        for (int i = 0; i < 64; i++) {
            Long finalI = Long.valueOf(i+1);
            executorService.submit(()-> run(controller, finalI));
        }
//        manager.removeAllClans();
//        manager.removeAllTransactions();
        executor.submit(()->manager.stop());
        executorService.shutdown();
        executor.shutdown();
    }


    private static void run(ClanController controller, Long i){
        Clan clan = controller.getClan(i);
//        Clan clan = controller.saveClan(new Clan(Thread.currentThread().getName(), new AtomicInteger(0)));
        for (int j = 0; j < 1000; j++) {
            controller.clanTaskPlus(clan.getId());
        }
        for (int j = 0; j < 1001; j++) {
            controller.clanTaskMinus(clan.getId());
        }

    }
}
