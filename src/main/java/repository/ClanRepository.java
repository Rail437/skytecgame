package repository;

import model.Clan;

import java.util.List;

public interface ClanRepository {
    Clan save(Clan clan);
    Clan findById(Long id);
    List<Clan> findAllClans();
    void printClansTable();
    void removeAllClans();
}
