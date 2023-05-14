package tuvarna.ticket_center_server.models;

import jakarta.persistence.*;
import tuvarna.ticket_center_server.enumerables.LogLevels;

import java.time.LocalDateTime;

@Entity(name = "LOGS")
public class Log {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ERROR_TIMESTAMP")
    private LocalDateTime errorTimestamp;

    @Column(name = "LOG_LEVEL")
    private LogLevels logLevel;

    @Column(name = "ERROR_FUNCTION")
    private String errorFunction;

    @Column(name = "CONTEXT")
    private String context;

    @Column(name = "CURRENT_METHOD")
    private String currentMethod;

    public Log() {
    }

    public Log(LocalDateTime timestamp, LogLevels logLevel, String function, String context, String currentMethod) {
        this.errorTimestamp = timestamp;
        this.logLevel = logLevel;
        this.errorFunction = function;
        this.context = context;
        this.currentMethod = currentMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getErrorTimestamp() {
        return errorTimestamp;
    }

    public void setErrorTimestamp(LocalDateTime timestamp) {
        this.errorTimestamp = timestamp;
    }

    public LogLevels getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevels logLevel) {
        this.logLevel = logLevel;
    }

    public String getErrorFunction() {
        return errorFunction;
    }

    public void setErrorFunction(String function) {
        this.errorFunction = function;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCurrentMethod() {
        return currentMethod;
    }

    public void setCurrentMethod(String currentMethod) {
        this.currentMethod = currentMethod;
    }
}
