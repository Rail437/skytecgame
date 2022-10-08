package service;

import lombok.RequiredArgsConstructor;
import model.Clan;
import model.Log;

import java.sql.Date;

// Еще один такой сервис
@RequiredArgsConstructor
public class TaskService { // какой-то сервис с заданиями
    private final ClanService clanService;
    private final LogService logService;

    public void completeTask(Long clanId, Long taskId) {
        // if (success)
        {
            Clan clan = clanService.getClan(clanId);
            clan.addGold(100);
            logService.log(new Log(new Date(System.currentTimeMillis()),"Clan "+clanId +" completed the task: "+ taskId+ " and got the gold. Total gold: " + clan.getGold()));
            clanService.save(clan);
        }
    }
}
