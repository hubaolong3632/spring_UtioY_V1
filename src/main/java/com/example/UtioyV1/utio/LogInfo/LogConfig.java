package com.example.UtioyV1.utio.LogInfo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "config.log")
@Data
public class LogConfig {

    // 日志配置静态字段
    public static boolean LOG_ENABLED = true;
    public static boolean LOG_CONSOLE_OUTPUT = true;
    public static boolean LOG_FILE_OUTPUT = true;
    public static long LOG_FLUSH_INTERVAL_SECONDS = 2;
    public static int LOG_BATCH_SIZE = 100;
    public static int LOG_KEEP_FILES = 30;  // 每个级别/年/月目录下保留最新的文件数量
    
    // 日志级别启用状态映射（级别名称 -> 是否启用）
    public static Map<String, Boolean> LOG_LEVELS_ENABLED = new HashMap<>();
    
    static {
        // 默认所有级别都启用
        LOG_LEVELS_ENABLED.put("INFO", true);
        LOG_LEVELS_ENABLED.put("ERROR", true);
        LOG_LEVELS_ENABLED.put("DEBUG", true);
        LOG_LEVELS_ENABLED.put("WARN", true);
        LOG_LEVELS_ENABLED.put("SEVERE", true);
        LOG_LEVELS_ENABLED.put("DAO", true);
    }

    // 非 static 字段（可以被 JSON 序列化）
    private Boolean enabled;
    private Boolean consoleOutput;
    private Boolean fileOutput;
    private Long flushIntervalSeconds;
    private Integer batchSize;
    private Integer keepFiles;  // 每个级别/年/月目录下保留最新的文件数量
    private LogLevelsConfig levels;  // 各个日志级别的启用状态
    
    // 日志配置 setter
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
        if (enabled != null) {
            LogConfig.LOG_ENABLED = enabled;
        }
    }

    public void setConsoleOutput(Boolean consoleOutput) {
        this.consoleOutput = consoleOutput;
        if (consoleOutput != null) {
            LogConfig.LOG_CONSOLE_OUTPUT = consoleOutput;
        }
    }

    public void setFileOutput(Boolean fileOutput) {
        this.fileOutput = fileOutput;
        if (fileOutput != null) {
            LogConfig.LOG_FILE_OUTPUT = fileOutput;
        }
    }

    public void setFlushIntervalSeconds(Long flushIntervalSeconds) {
        this.flushIntervalSeconds = flushIntervalSeconds;
        if (flushIntervalSeconds != null) {
            LogConfig.LOG_FLUSH_INTERVAL_SECONDS = flushIntervalSeconds;
        }
    }

    public void setBatchSize(Integer batchSize) {
        this.batchSize = batchSize;
        if (batchSize != null) {
            LogConfig.LOG_BATCH_SIZE = batchSize;
        }
    }

    public void setKeepFiles(Integer keepFiles) {
        this.keepFiles = keepFiles;
        if (keepFiles != null) {
            LogConfig.LOG_KEEP_FILES = keepFiles;
        }
    }

    public void setLevels(LogLevelsConfig levels) {
        this.levels = levels;
        if (levels != null) {
            if (levels.getInfo() != null) {
                LogConfig.LOG_LEVELS_ENABLED.put("INFO", levels.getInfo());
            }
            if (levels.getError() != null) {
                LogConfig.LOG_LEVELS_ENABLED.put("ERROR", levels.getError());
            }
            if (levels.getDebug() != null) {
                LogConfig.LOG_LEVELS_ENABLED.put("DEBUG", levels.getDebug());
            }
            if (levels.getWarn() != null) {
                LogConfig.LOG_LEVELS_ENABLED.put("WARN", levels.getWarn());
            }
            if (levels.getSevere() != null) {
                LogConfig.LOG_LEVELS_ENABLED.put("SEVERE", levels.getSevere());
            }
            if (levels.getDao() != null) {
                LogConfig.LOG_LEVELS_ENABLED.put("DAO", levels.getDao());
            }
        }
    }
    
    // 日志级别配置内部类
    @Data
    public static class LogLevelsConfig {
        private Boolean info;     // INFO级别是否启用
        private Boolean error;   // ERROR级别是否启用
        private Boolean debug;   // DEBUG级别是否启用
        private Boolean warn;    // WARN级别是否启用
        private Boolean severe;  // SEVERE级别是否启用
        private Boolean dao;  // dao数据库级别是否启用
    }
}

