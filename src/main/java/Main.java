import controller.ClanController;
import model.Clan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        ClanController controller = new ClanController();
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        for (int i = 0; i < 32; i++) {
            executorService.submit(()->{
                Clan clan = controller.saveClan(new Clan(Thread.currentThread().getName(), new AtomicInteger(10)));
                for (int j = 0; j < 500; j++) {
                    controller.clanTaskPlus(clan.getId());
                }
                for (int j = 0; j < 500; j++) {
                    controller.clanTaskMinus(clan.getId());
                }

            });
        }
//        controller.userAddGold(333L, clan.getId(),1000);
//        controller.clanTaskComplete(clan.getId(), 555L);
//        controller.removeAllClans();
//        controller.remomveAllLogs();
//        controller.removeAllTransactions();
        controller.printClansTable();
//        controller.printLogsTable();
//        controller.printAllTransactionByClanId(46L);
        executorService.shutdown();
    }
}
