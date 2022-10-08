package service;

import lombok.RequiredArgsConstructor;
import model.Clan;
import model.Log;
import model.Transaction;

import java.sql.Date;
import java.util.Random;

// Еще один такой сервис
@RequiredArgsConstructor
public class TaskService { // какой-то сервис с заданиями
    private final MainService mainService;
    private int minus = 30; //количество золота которое теряют при не успешном прохождении задания.

    public void completeTask(Long clanId, Long taskId) {
        // if (success)
        {
            Clan clan = mainService.getClan(clanId);
            clan.addGold(100);
            mainService.log(new Log(new Date(System.currentTimeMillis()), "Clan " + clanId + " completed the task: " + taskId + " and got the gold. Total gold: " + clan.getGold()));
            mainService.save(clan);
        }
    }

    public void clanRunRandomTask(Long clanId) {
        Random random = new Random();
        Clan clan = mainService.getClan(clanId);
        if (random.nextBoolean()) {
            int i = 10 + random.nextInt(1000);
            mainService.save(new Transaction(clanId, new Date(System.currentTimeMillis()), clan.getGold(), i));
            clan.addGold(i);
            mainService.log(new Log(new Date(System.currentTimeMillis()), "Clan " + clanId + " completed the task: " + true + ". Total gold: " + clan.getGold()));
            mainService.save(clan);
        } else {
            mainService.save(new Transaction(clanId, new Date(System.currentTimeMillis()), clan.getGold(), minus));
            clan.minusGold(minus);
            mainService.log(new Log(new Date(System.currentTimeMillis()), "Clan " + clanId + " completed the task: " + false + ". Total gold: " + clan.getGold()));
            mainService.save(clan);
        }

    }

    public void runTaskPlus(Long clanId) {
        Clan clan = mainService.getClan(clanId);
        clan.addGold(10);
        mainService.save(new Transaction(clanId, new Date(System.currentTimeMillis()), clan.getGold(), 10));
        mainService.log(new Log(new Date(System.currentTimeMillis()), "Clan " + clanId + " completed the task: " + true + ". Total gold: " + clan.getGold()));
        mainService.save(clan);
    }

    public void runTaskMinus(Long clanId) {
        minus = -10;
        Clan clan = mainService.getClan(clanId);
        clan.minusGold(minus);
        mainService.save(new Transaction(clanId, new Date(System.currentTimeMillis()), clan.getGold(), minus));
        mainService.log(new Log(new Date(System.currentTimeMillis()), "Clan " + clanId + " completed the task: " + false + ". Total gold: " + clan.getGold()));
        mainService.save(clan);
    }
}
