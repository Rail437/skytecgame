package service;

import lombok.RequiredArgsConstructor;
import model.Clan;
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
            mainService.save(clan);
        }
    }

    public synchronized void runTaskPlus(Long clanId) {
        Clan clan = mainService.getClan(clanId);
        int baseGold = clan.getGold();
        clan.addGold(10);
        mainService.save(new Transaction(clanId, new Date(System.currentTimeMillis()), baseGold, 10, "text"));
        mainService.update(clan);
    }

    public synchronized void runTaskMinus(Long clanId) {
        minus = 10;
        Clan clan = mainService.getClan(clanId);
        int baseGold = clan.getGold();
        clan.minusGold(minus);
        mainService.save(new Transaction(clanId, new Date(System.currentTimeMillis()), baseGold, -minus, "text"));
        mainService.update(clan);
    }
}
