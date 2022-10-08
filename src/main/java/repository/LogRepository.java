package repository;


import model.Log;

import java.util.List;

public interface LogRepository {
    Log save(Log log);
    void saveInBatch(List<Log>logs);
    Log findByid(Long id);
}
