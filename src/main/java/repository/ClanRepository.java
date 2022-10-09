package repository;

import model.Clan;

import java.util.List;

public interface ClanRepository {
    Clan save(Clan clan);
    Clan findById(Long id);
}
