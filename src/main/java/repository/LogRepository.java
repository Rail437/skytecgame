package repository;


import model.Log;

public interface LogRepository {
    Log save(Log log);
    Log findByid(Long id);
    void printLogTable();
    void clearAllLog();

    void printLogsByClanId(Long clanId);
}
