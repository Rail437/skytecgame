import controller.ClanController;
import manager.QueueManager;
import model.Clan;
import repository.*;

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
        for (int i = 0; i < 64; i++) {
            executorService.submit(()-> run(controller));
        }
        executor.submit(()->manager.start());
//        manager.removeAllClans();
//        manager.removeAllTransactions();
        System.out.println("stop");
        executor.submit(()->manager.stop());
        executorService.shutdown();
        executor.shutdown();
    }


    private static void run(ClanController controller){
        Clan clan = controller.saveClan(new Clan(Thread.currentThread().getName(), new AtomicInteger(10)));
        for (int j = 0; j < 500; j++) {
            controller.clanTaskPlus(clan.getId());
        }
        for (int j = 0; j < 500; j++) {
            controller.clanTaskMinus(clan.getId());
        }

    }
}
