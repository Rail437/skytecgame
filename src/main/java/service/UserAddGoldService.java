package service;

import lombok.RequiredArgsConstructor;
import model.Clan;
import model.Log;

import java.sql.Date;

// Так же у нас есть ряд сервисов похожих на эти.
// Их суть в том, что они добавляют(или уменьшают) золото в казне клана
@RequiredArgsConstructor
public class UserAddGoldService { // пользователь добавляет золото из собственного кармана
    private final ClanService clanService;
    private final LogService logService;

    public void addGoldToClan(long userId, long clanId, int gold) {
        Clan clan = clanService.getClan(clanId);
        clan.addGold(gold);
        logService.log(new Log(new Date(System.currentTimeMillis()), "User: " + userId + " added " + gold + " gold in Clan " + clanId));
        clanService.save(clan);
    }
}
