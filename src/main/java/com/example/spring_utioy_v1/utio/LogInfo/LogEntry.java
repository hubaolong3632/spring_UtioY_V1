package com.example.spring_utioy_v1.utio.LogInfo;

/**
 * 日志条目类
 */
public class LogEntry {
    private final LogLevel level;
    private final String message;
    private final String type;
    private final long timestamp;
    
    public LogEntry(LogLevel level, String message, String type) {
        this.level = level;
        this.message = message;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }
    
    public LogLevel getLevel() {
        return level;
    }
    
    public String getMessage() {
        return message;
    }
    
    public String getType() {
        return type;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}

