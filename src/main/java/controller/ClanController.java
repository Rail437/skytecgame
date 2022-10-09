package controller;

import manager.QueueManager;
import model.Clan;
import service.MainService;
import service.TaskService;
import service.UserAddGoldService;

public class ClanController {
    private final TaskService taskService;
    private final UserAddGoldService userAddGoldService;
    private final MainService mainService;

    public ClanController(QueueManager queueManager) {
        this.mainService = new MainService(queueManager);
        this.taskService = new TaskService(mainService);
        this.userAddGoldService = new UserAddGoldService(mainService);
    }

    public Clan saveClan(Clan clan) {
        return mainService.save(clan);
    }

    public void clanTaskComplete(Long clanid, Long taskId) {
        taskService.completeTask(clanid, taskId);
    }


//    public void removeAllClans() {
//        mainService.removeAllClans();
//    }


//    public void printClansTable() {mainService.printClansTable();}


    public void userAddGold(Long userId,Long clanId, int gold){
        userAddGoldService.addGoldToClan(userId,clanId,gold);
    }


//    public void removeAllTransactions() {
//        mainService.deleteAllTransactions();
//    }

    public void clanTaskPlus(Long id) {
        taskService.runTaskPlus(id);
    }
    public void clanTaskMinus(Long id) {
        taskService.runTaskMinus(id);
    }

//    public void printAllTransactionByClanId(long id) {
//        mainService.printAllTransactionsByClanId(id);
//    }
}
