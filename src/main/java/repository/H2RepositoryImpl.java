package repository;

import model.Clan;
import model.Log;

import java.util.List;

public class H2RepositoryImpl implements ClanRepository, LogRepository {
    H2JDBCUtils jdbcUtils = new H2JDBCUtils();

    @Override
    public Clan save(Clan clan) {
        return jdbcUtils.save(clan);
    }

    @Override
    public Clan findById(Long id) {
        return jdbcUtils.findById(id);
    }

    @Override
    public List<Clan> findAllClans() {
        return jdbcUtils.findAllClans();
    }

    public void printClansTable(){
        jdbcUtils.printClansTable();
    }

    public void removeAllClans() {
        jdbcUtils.removeAllClans();
    }

    @Override
    public Log save(Log log) {
        return jdbcUtils.insertLog(log);
    }

    @Override
    public void saveInBatch(List<Log> logs) {
        jdbcUtils.insertLogInBatch(logs);
    }

    @Override
    public Log findByid(Long id) {
        return jdbcUtils.findLogById(id);
    }

    public void printLogTable() {
        jdbcUtils.printLogsTable();
    }

    public void removeAllLogs(){
        jdbcUtils.removeAllLogs();
    }
}
