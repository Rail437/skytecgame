package controller;

import lombok.RequiredArgsConstructor;
import model.Clan;
import service.ClanService;
import service.TaskService;
import service.UserAddGoldService;

@RequiredArgsConstructor
public class ClanController {
    private final ClanService clanService;
    private final TaskService taskService;
    private final UserAddGoldService userAddGoldService;

    public Clan saveClan(Clan clan){
        return clanService.save(clan);
    }

    public void clanTaskComplete(Long clanid, Long taskId){
        taskService.completeTask(clanid,taskId);
    }
}
