package service;

import lombok.RequiredArgsConstructor;
import model.Clan;
import model.Transaction;

import java.sql.Date;

// Так же у нас есть ряд сервисов похожих на эти.
// Их суть в том, что они добавляют(или уменьшают) золото в казне клана
@RequiredArgsConstructor
public class UserAddGoldService { // пользователь добавляет золото из собственного кармана
    private final MainService mainService;

    public void addGoldToClan(long userId, long clanId, int gold) {
        Clan clan = mainService.getClan(clanId);
        int baseGold = clan.getGold();
        clan.addGold(gold);
        mainService.save(new Transaction(clanId,new Date(System.currentTimeMillis()),baseGold,gold,"user: userid "+ userId +" added gold"));
        mainService.save(clan);
    }
}
