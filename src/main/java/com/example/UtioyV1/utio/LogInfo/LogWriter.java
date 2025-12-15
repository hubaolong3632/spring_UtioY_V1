package com.example.UtioyV1.utio.LogInfo;

import com.example.UtioyV1.utio.Code.Config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志文件写入器
 */
public class LogWriter {
    
    // 日志目录
    private static final String LOG_DIR = System.getProperty("user.dir") + File.separator + "log";
    
    // 线程安全的日期格式化器
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy");
    private static final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
    private static final DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");
    
    // 文件写入器缓存（按文件路径缓存，避免频繁打开/关闭文件）
    private static final Map<String, BufferedWriter> writerCache = new ConcurrentHashMap<>();
    
    /**
     * 批量写入日志到文件
     */
    public static void writeBatchToFile(List<LogEntry> entries) {
        // 按日志级别、年份、月份和日期分组
        Map<String, List<LogEntry>> groupedEntries = new ConcurrentHashMap<>();
        
        for (LogEntry entry : entries) {

            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    entry.getCreate_time().toInstant(),
                    ZoneId.systemDefault()
            );
//           Instant instant = java.time.Instant.ofEpochMilli(dateTime);


//            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault());
            
            String yearStr = dateTime.format(yearFormatter);
            String monthStr = dateTime.format(monthFormatter);
            String dayStr = dateTime.format(dayFormatter);
            String levelStr = entry.getLevel().name().toLowerCase();
            // 文件键格式：级别/年/月/日，例如：error/2024/01/21
            String fileKey = levelStr + "/" + yearStr + "/" + monthStr + "/" + dayStr;
            
            groupedEntries.computeIfAbsent(fileKey, k -> new ArrayList<>()).add(entry);
        }
        
        // 按文件分组写入
        for (Map.Entry<String, List<LogEntry>> group : groupedEntries.entrySet()) {
            String fileKey = group.getKey();
            List<LogEntry> fileEntries = group.getValue();
            
            try {
                BufferedWriter writer = getWriter(fileKey);
                for (LogEntry entry : fileEntries) {
//                    java.time.Instant instant = java.time.Instant.ofEpochMilli(entry.getTimestamp());
//                    LocalDateTime dateTime = LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault());
                    LocalDateTime dateTime = LocalDateTime.ofInstant(
                            entry.getCreate_time().toInstant(),
                            ZoneId.systemDefault()
                    );
                    String timeStr = dateTime.format(timeFormatter);
                    String levelStr = entry.getLevel().name().toUpperCase();
                    String typeStr = entry.getType() != null ? entry.getType() : "";
                    
                    // 格式：时间 | 日志类别 | 类型 | 日志信息
                    writer.write(timeStr + " | " + levelStr + " | " + typeStr + " | " + entry.getMessage());
                    writer.newLine();
                }
                writer.flush(); // 批量刷新
            } catch (IOException e) {
                System.err.println("Failed to write log to file: " + e.getMessage());
            }
        }
    }
    
    /**
     * 获取或创建文件写入器（复用文件句柄）
     * 文件键格式：级别/年/月/日，例如：error/2024/01/21
     * 文件路径：log/error/2024/01/error_21.log
     */
    private static BufferedWriter getWriter(String fileKey) throws IOException {
        return writerCache.computeIfAbsent(fileKey, key -> {
            try {
                // 文件键格式：级别/年/月/日
                String[] parts = key.split("/");
                String levelStr = parts[0];  // 级别：error, info, debug
                String yearStr = parts[1];   // 年份：2024
                String monthStr = parts[2];  // 月份：01
                String dayStr = parts[3];    // 日期：21
                
                // 创建目录结构：log/级别/年/月/
                File monthDir = new File(LOG_DIR, levelStr + File.separator + yearStr + File.separator + monthStr);
                if (!monthDir.exists()) {
                    monthDir.mkdirs();
                }
                
                // 文件名为：级别_日.log，例如：error_21.log
                String fileName = levelStr + "_" + dayStr + ".log";
                File logFile = new File(monthDir, fileName);
                
                // 清理旧文件（保留最新的x份）
                cleanupOldFiles(monthDir, levelStr);
                
                return new BufferedWriter(new FileWriter(logFile, true), 8192); // 8KB缓冲区
            } catch (IOException e) {
                throw new RuntimeException("Failed to create log writer", e);
            }
        });
    }
    
    /**
     * 关闭所有文件写入器
     */
    public static void closeAllWriters() {
        for (Map.Entry<String, BufferedWriter> entry : writerCache.entrySet()) {
            try {
                entry.getValue().close();
            } catch (IOException e) {
                System.err.println("Failed to close writer for " + entry.getKey() + ": " + e.getMessage());
            }
        }
        writerCache.clear();
    }
    
    /**
     * 初始化日志目录
     */
    public static void initializeLogDir() {
        if (Config.LOG_FILE_OUTPUT) {
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
        }
    }
    
    /**
     * 清理旧日志文件，只保留最新的x份
     * @param monthDir 月份目录，例如：log/error/2024/01
     * @param levelStr 日志级别，例如：error
     */
    private static void cleanupOldFiles(File monthDir, String levelStr) {
        if (!monthDir.exists() || !monthDir.isDirectory()) {
            return;
        }
        
        int keepFiles = Config.LOG_KEEP_FILES;
        if (keepFiles <= 0) {
            return; // 如果配置为0或负数，不清理
        }
        
        // 获取该目录下所有匹配的日志文件（格式：级别_日.log）
        File[] logFiles = monthDir.listFiles((dir, name) -> 
            name.startsWith(levelStr + "_") && name.endsWith(".log")
        );
        
        if (logFiles == null || logFiles.length <= keepFiles) {
            return; // 文件数量未超过限制，不需要清理
        }
        
        // 按文件名排序（文件名包含日期，自然排序即可）
        Arrays.sort(logFiles, Comparator.comparing(File::getName).reversed());
        
        // 删除超出保留数量的旧文件
        for (int i = keepFiles; i < logFiles.length; i++) {
            try {
                if (logFiles[i].delete()) {
                    System.out.println("Deleted old log file: " + logFiles[i].getAbsolutePath());
                }
            } catch (Exception e) {
                System.err.println("Failed to delete old log file: " + logFiles[i].getAbsolutePath() + 
                                 ", error: " + e.getMessage());
            }
        }
    }
}

