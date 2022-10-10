package repository;

import model.Clan;

public interface ClanRepository {
    Clan save(Clan clan);
    Clan findById(Long id);
}
