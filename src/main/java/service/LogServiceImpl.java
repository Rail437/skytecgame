package service;

import model.Log;
import org.apache.commons.collections4.ListUtils;
import repository.LogRepository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class LogServiceImpl implements LogService {
    private final LogRepository logRepository;
    private List<Log> logsQueue = new CopyOnWriteArrayList<>();

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public synchronized void log(Log log) {
        logsQueue.add(log);
    }

    public synchronized void check(){
        if(logsQueue.size() > 10){
            List<Log> logs = logsQueue.stream().limit(10).collect(Collectors.toList());
            logRepository.saveInBatch(logs);
            logsQueue.retainAll(logs);
        }else {
            for (Log log : logsQueue) {
                logRepository.save(log);
            }
        }
    }
}
