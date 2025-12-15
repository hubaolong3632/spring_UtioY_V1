package com.example.UtioyV1.utio.Code;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "config")
@Data  // 添加 Lombok
public class Config {

    // static 字段（给其他地方直接用）
    public static String CLAIMS_JWS_ATTRIBUTE = "CLAIMS_JWS_ATTRIBUTE";
    public static String currentPath = System.getProperty("user.dir") + "/file";
    public static String FILE_URL = "https://smartfarmservice.00000.work";
    
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
    }

    // 非 static 字段（可以被 JSON 序列化）
    private String currentPath1;
    private String fileUrl;
    
    // 日志配置内部类
    private LogConfig log;

    // setter 注入后赋值给 static
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
        Config.FILE_URL = fileUrl;
    }

    public void setCurrentPath(String currentPath) {
        Config.currentPath = currentPath;
    }
    
    // 日志配置 setter
    public void setLog(LogConfig log) {
        this.log = log;
        if (log != null) {
            Config.LOG_ENABLED = log.getEnabled() != null ? log.getEnabled() : true;
            Config.LOG_CONSOLE_OUTPUT = log.getConsoleOutput() != null ? log.getConsoleOutput() : true;
            Config.LOG_FILE_OUTPUT = log.getFileOutput() != null ? log.getFileOutput() : true;
            Config.LOG_FLUSH_INTERVAL_SECONDS = log.getFlushIntervalSeconds() != null ? log.getFlushIntervalSeconds() : 2;
            Config.LOG_BATCH_SIZE = log.getBatchSize() != null ? log.getBatchSize() : 100;
            Config.LOG_KEEP_FILES = log.getKeepFiles() != null ? log.getKeepFiles() : 30;
            
            // 设置各个日志级别的启用状态
            if (log.getLevels() != null) {
                if (log.getLevels().getInfo() != null) {
                    Config.LOG_LEVELS_ENABLED.put("INFO", log.getLevels().getInfo());
                }
                if (log.getLevels().getError() != null) {
                    Config.LOG_LEVELS_ENABLED.put("ERROR", log.getLevels().getError());
                }
                if (log.getLevels().getDebug() != null) {
                    Config.LOG_LEVELS_ENABLED.put("DEBUG", log.getLevels().getDebug());
                }
                if (log.getLevels().getWarn() != null) {
                    Config.LOG_LEVELS_ENABLED.put("WARN", log.getLevels().getWarn());
                }
                if (log.getLevels().getSevere() != null) {
                    Config.LOG_LEVELS_ENABLED.put("SEVERE", log.getLevels().getSevere());
                }
            }
        }
    }
    
    // 日志配置内部类
    @Data
    public static class LogConfig {
        private Boolean enabled;
        private Boolean consoleOutput;
        private Boolean fileOutput;
        private Long flushIntervalSeconds;
        private Integer batchSize;
        private Integer keepFiles;  // 每个级别/年/月目录下保留最新的文件数量
        private LogLevelsConfig levels;  // 各个日志级别的启用状态
    }
    
    // 日志级别配置内部类
    @Data
    public static class LogLevelsConfig {
        private Boolean info;     // INFO级别是否启用
        private Boolean error;   // ERROR级别是否启用
        private Boolean debug;   // DEBUG级别是否启用
        private Boolean warn;    // WARN级别是否启用
        private Boolean severe;  // SEVERE级别是否启用
    }
}