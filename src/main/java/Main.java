import controller.ClanController;
import model.Clan;
import repository.H2RepositoryImpl;
import service.*;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        H2RepositoryImpl h2Repository = new H2RepositoryImpl();
        ClanService clanService = new ClanServiceImpl(h2Repository);
        LogService logService = new LogServiceImpl(h2Repository);
        ClanController controller = new ClanController(
                clanService,
                new TaskService(clanService,logService),
                new UserAddGoldService(clanService,logService));
        Clan clan = controller.saveClan(new Clan("Clan", new AtomicInteger(666)));
        for (int i = 0; i < 100; i++) {
            controller.clanTaskComplete(clan.getId(), 555L + i);
        }
//        controller.clanTaskComplete(clan.getId(), 555L);

//        h2Repository.removeAllClans();
//        h2Repository.removeAllLogs();
        h2Repository.printClansTable();
        h2Repository.printLogTable();
    }
}
