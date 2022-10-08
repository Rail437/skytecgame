package service;

import model.Clan;
import repository.ClanRepository;

public class ClanServiceImpl implements ClanService {
    private final ClanRepository clanRepository;

    public ClanServiceImpl(ClanRepository clanRepository) {
        this.clanRepository = clanRepository;
    }

    @Override
    public Clan get(Long clanId) {
        return clanRepository.findById(clanId);
    }

    @Override
    public Clan save(Clan clan) {
        return clanRepository.save(clan);
    }

    @Override
    public Clan getClan(Long clanId) {
        return clanRepository.findById(clanId);
    }

    @Override
    public void removeAllClans() {
        clanRepository.removeAllClans();
    }

    @Override
    public void printClansTable() {
        clanRepository.printClansTable();
    }

}
