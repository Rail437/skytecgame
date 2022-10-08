package service;

import model.Log;

public interface LogService {
    void log(Log log);

    void removeAllLogs();

    void printLogTable();

    void printAllLogByClanId(Long clanId);
}
