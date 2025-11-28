package com.example.spring_utioy_v1.utio;

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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

//日志功能
public class Log {
    
    // 日志类别枚举
    private enum LogLevel {
        INFO, ERROR, DEBUG
    }
    
    // 日志条目类
    private static class LogEntry {
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
    
    // 日志队列（限制大小，防止内存溢出）
    private static final int MAX_QUEUE_SIZE = 100000; // 最大队列大小
    private static final BlockingQueue<LogEntry> logQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
    
    // 日志目录
    private static final String LOG_DIR = System.getProperty("user.dir") + File.separator + "log";
    
    // 线程安全的日期格式化器（Java 8+）
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter fileDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // 文件写入器缓存（按文件路径缓存，避免频繁打开/关闭文件）
    private static final Map<String, BufferedWriter> writerCache = new ConcurrentHashMap<>();
    
    // 批量写入配置（从配置文件读取，默认值作为后备）
    private static int getBatchSize() {
        return Config.LOG_BATCH_SIZE > 0 ? Config.LOG_BATCH_SIZE : 100;
    }
    
    private static long getFlushIntervalMs() {
        return Config.LOG_FLUSH_INTERVAL_SECONDS > 0 ? Config.LOG_FLUSH_INTERVAL_SECONDS * 1000 : 2000;
    }
    
    // 写入线程数量（可根据CPU核心数调整）
    private static final int WRITER_THREAD_COUNT = Math.max(2, Runtime.getRuntime().availableProcessors() / 2);
    
    // 后台线程
    private static volatile boolean running = true;
    private static Thread[] logThreads;
    
    // 初始化日志线程
    static {
        initializeLogThread();
    }
    
    // 初始化日志处理线程（多线程版本）
    private static void initializeLogThread() {
        // 如果日志未启用，不启动线程
        if (!Config.LOG_ENABLED) {
            return;
        }
        
        // 如果文件输出未启用，不创建日志目录
        if (Config.LOG_FILE_OUTPUT) {
            // 创建日志目录
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
        }
        
        // 如果文件输出未启用，不启动写入线程
        if (!Config.LOG_FILE_OUTPUT) {
            return;
        }
        
        // 启动多个后台写入线程（提高并发处理能力）
        logThreads = new Thread[WRITER_THREAD_COUNT];
        for (int i = 0; i < WRITER_THREAD_COUNT; i++) {
            final int threadId = i;
            logThreads[i] = new Thread(() -> {
                int batchSize = getBatchSize();
                long flushIntervalMs = getFlushIntervalMs();
                List<LogEntry> batch = new ArrayList<>(batchSize);
                long lastFlushTime = System.currentTimeMillis();
                
                while (running || !logQueue.isEmpty()) {
                    try {
                        // 批量收集日志（使用drainTo提高效率）
                        int drained = logQueue.drainTo(batch, batchSize);
                        if (drained == 0) {
                            // 如果队列为空，等待一段时间
                            LogEntry entry = logQueue.poll(100, TimeUnit.MILLISECONDS);
                            if (entry != null) {
                                batch.add(entry);
                            }
                        }
                        
                        long currentTime = System.currentTimeMillis();
                        boolean shouldFlush = batch.size() >= batchSize || 
                                             (currentTime - lastFlushTime >= flushIntervalMs && !batch.isEmpty()) ||
                                             (!running && logQueue.isEmpty() && !batch.isEmpty());
                        
                        if (shouldFlush && !batch.isEmpty()) {
                            writeBatchToFile(batch);
                            batch.clear();
                            lastFlushTime = currentTime;
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
                
                // 处理剩余的日志
                if (!batch.isEmpty()) {
                    writeBatchToFile(batch);
                }
            }, "LogWriterThread-" + threadId);
            
            logThreads[i].setDaemon(true);
            logThreads[i].start();
        }
        
        // 注册JVM关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            shutdown();
        }));
    }
    
    // 批量写入日志到文件
    private static void writeBatchToFile(List<LogEntry> entries) {
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
    
    // 获取或创建文件写入器（复用文件句柄）
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
    
    // 关闭所有文件写入器
    private static void closeAllWriters() {
        for (Map.Entry<String, BufferedWriter> entry : writerCache.entrySet()) {
            try {
                entry.getValue().close();
            } catch (IOException e) {
                System.err.println("Failed to close writer for " + entry.getKey() + ": " + e.getMessage());
            }
        }
        writerCache.clear();
    }
    
    // 关闭日志系统
    public static void shutdown() {
        running = false;
        if (logThreads != null) {
            for (Thread thread : logThreads) {
                if (thread != null) {
                    thread.interrupt();
                    try {
                        thread.join(5000); // 等待最多5秒
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        
        // 处理队列中剩余的日志
        List<LogEntry> remaining = new ArrayList<>();
        logQueue.drainTo(remaining);
        if (!remaining.isEmpty()) {
            writeBatchToFile(remaining);
        }
        
        closeAllWriters();
    }
    
    // 添加日志到队列（带type参数）
    private static void addLog(LogLevel level, String msg, String type) {
        // 如果日志未启用，直接返回
        if (!Config.LOG_ENABLED) {
            return;
        }
        
        // 根据配置决定是否输出到控制台
        if (Config.LOG_CONSOLE_OUTPUT) {
            if (level == LogLevel.ERROR) {
                System.err.println(msg);
            } else {
                System.out.println(msg);
            }
        }
        
        // 如果文件输出未启用，不添加到队列
        if (!Config.LOG_FILE_OUTPUT) {
            return;
        }
        
        // 添加到队列（非阻塞，如果队列满则丢弃）
        if (!logQueue.offer(new LogEntry(level, msg, type))) {
            // 队列满时的处理：记录警告并丢弃
            if (Config.LOG_CONSOLE_OUTPUT) {
                System.err.println("WARNING: Log queue is full (" + logQueue.size() + "), dropping log: " + 
                                 (msg.length() > 100 ? msg.substring(0, 100) + "..." : msg));
            }
        }
    }
    
    // 添加日志到队列（不带type参数，向后兼容）
    private static void addLog(LogLevel level, String msg) {
        addLog(level, msg, null);
    }
    
    // 获取当前队列大小（用于监控）
    public static int getQueueSize() {
        return logQueue.size();
    }
    
    // 获取队列剩余容量
    public static int getRemainingCapacity() {
        return logQueue.remainingCapacity();
    }
    
    // 带type参数的日志方法
    public static void info(String msg, String type) {
        addLog(LogLevel.INFO, msg, type);
    }

    public static void error(String msg, String type) {
        addLog(LogLevel.ERROR, msg, type);
    }

    public static void debug(String msg, String type) {
        addLog(LogLevel.DEBUG, msg, type);
    }
    
    // 不带type参数的日志方法（向后兼容，type为null）
    public static void info(String msg) {
        addLog(LogLevel.INFO, msg);
    }

    public static void error(String msg) {
        addLog(LogLevel.ERROR, msg);
    }

    public static void debug(String msg) {
        addLog(LogLevel.DEBUG, msg);
    }
}
