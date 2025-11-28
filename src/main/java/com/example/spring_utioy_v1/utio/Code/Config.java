package com.example.spring_utioy_v1.utio.Code;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

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
    }
}