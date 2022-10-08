package controller;

import model.Clan;
import repository.*;
import service.*;

public class ClanController {
    private final ClanService clanService;
    private final TaskService taskService;
    private final UserAddGoldService userAddGoldService;
    private final MainService mainService;

    public ClanController() {
        JDBCUtility jdbcUtility = new PostgresJDBCUtility();
        RepositoryImpl repository = new RepositoryImpl(jdbcUtility);
        TransactionRepository transactionRepository = new TransactionRepo(jdbcUtility);
        this.mainService = new MainService(repository, repository,transactionRepository);
        this.clanService = new ClanServiceImpl(repository);
        this.taskService = new TaskService(mainService);
        this.userAddGoldService = new UserAddGoldService(mainService, mainService);
    }

    public Clan saveClan(Clan clan) {
        return clanService.save(clan);
    }

    public void clanTaskComplete(Long clanid, Long taskId) {
        taskService.completeTask(clanid, taskId);
    }

    public void clanRunRandomTask(Long clanId){
        taskService.clanRunRandomTask(clanId);
    }

    public void removeAllClans() {
        mainService.removeAllClans();
    }

    public void remomveAllLogs() {
        mainService.removeAllLogs();
    }

    public void printClansTable() {mainService.printClansTable();
    }

    public void printLogsTable(){
        mainService.printLogTable();
    }

    public void userAddGold(Long userId,Long clanId, int gold){
        userAddGoldService.addGoldToClan(userId,clanId,gold);
    }

    public void printAllLogByClanId(Long clanId){
        mainService.printAllLogByClanId(clanId);
    }

    public void removeAllTransactions() {
        mainService.deleteAllTransactions();
    }

    public void clanTaskPlus(Long id) {
        taskService.runTaskPlus(id);
    }
    public void clanTaskMinus(Long id) {
        taskService.runTaskMinus(id);
    }

    public void printAllTransactionByClanId(long id) {
        mainService.printAllTransactionsByClanId(id);
    }
}
