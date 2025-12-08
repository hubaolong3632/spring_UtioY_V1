package com.example.spring_utioy_v1.utio.LogInfo;

import com.example.spring_utioy_v1.utio.Code.Config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private static final DateTimeFormatter fileDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // 文件写入器缓存（按文件路径缓存，避免频繁打开/关闭文件）
    private static final Map<String, BufferedWriter> writerCache = new ConcurrentHashMap<>();
    
    /**
     * 批量写入日志到文件
     */
    public static void writeBatchToFile(List<LogEntry> entries) {
        // 按日志级别和日期分组
        Map<String, List<LogEntry>> groupedEntries = new ConcurrentHashMap<>();
        
        for (LogEntry entry : entries) {
            java.time.Instant instant = java.time.Instant.ofEpochMilli(entry.getTimestamp());
            LocalDateTime dateTime = LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault());
            
            String dateStr = dateTime.format(fileDateFormatter);
            String levelStr = entry.getLevel().name().toLowerCase();
            String fileKey = levelStr + "_" + dateStr;
            
            groupedEntries.computeIfAbsent(fileKey, k -> new ArrayList<>()).add(entry);
        }
        
        // 按文件分组写入
        for (Map.Entry<String, List<LogEntry>> group : groupedEntries.entrySet()) {
            String fileKey = group.getKey();
            List<LogEntry> fileEntries = group.getValue();
            
            try {
                BufferedWriter writer = getWriter(fileKey);
                for (LogEntry entry : fileEntries) {
                    java.time.Instant instant = java.time.Instant.ofEpochMilli(entry.getTimestamp());
                    LocalDateTime dateTime = LocalDateTime.ofInstant(instant, java.time.ZoneId.systemDefault());
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
     */
    private static BufferedWriter getWriter(String fileKey) throws IOException {
        return writerCache.computeIfAbsent(fileKey, key -> {
            try {
                String[] parts = key.split("_");
                String levelStr = parts[0];
                String dateStr = parts[1];
                String fileName = levelStr + "_" + dateStr + ".log";
                File logFile = new File(LOG_DIR, fileName);
                
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
}

