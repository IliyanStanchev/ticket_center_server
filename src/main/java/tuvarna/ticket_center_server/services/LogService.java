package tuvarna.ticket_center_server.services;

import tuvarna.ticket_center_server.enumerables.LogLevels;

public interface LogService {

    void log(LogLevels level, String function, String context, String currentMethod);
}
