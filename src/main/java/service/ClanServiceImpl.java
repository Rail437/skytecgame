package service;

import model.Clan;
import repository.ClanRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClanServiceImpl implements ClanService {
    private final Map<Long, Clan> clanMap = new ConcurrentHashMap<>();
    private final ClanRepository clanRepository;

    public ClanServiceImpl(ClanRepository clanRepository) {
        this.clanRepository = clanRepository;
    }

    @Override
    public Clan get(Long clanId) {
        return clanMap.get(clanId);
    }

    @Override
    public Clan save(Clan clan) {
        Clan save = clanRepository.save(clan);
        clanMap.put(save.getId(),save);
        return save;
    }

    @Override
    public Clan getClan(Long clanId) {
        return clanMap.get(clanId);
    }

}
