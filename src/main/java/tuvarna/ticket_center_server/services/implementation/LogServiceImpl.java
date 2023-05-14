package tuvarna.ticket_center_server.services.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tuvarna.ticket_center_server.enumerables.LogLevels;
import tuvarna.ticket_center_server.models.Log;
import tuvarna.ticket_center_server.repositories.LogRepository;
import tuvarna.ticket_center_server.services.LogService;

import java.time.LocalDateTime;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    public void log(LogLevels level, String function, String context, String currentMethod) {

        Log log = new Log(LocalDateTime.now(), level, function, context, currentMethod);
        logRepository.save(log);
    }

}
