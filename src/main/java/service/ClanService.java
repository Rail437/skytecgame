package service;

import model.Clan;

// Есть сервис, посвященный кланам.
// Да это выглядит как 'репозиторий'.
// Но это сервис, просто все остппльные методы не нужны для примера
public interface ClanService {
    Clan get(Long clanId);

    Clan save(Clan clan);

    Clan getClan(Long clanId);

    void removeAllClans();

    void printClansTable();
}
