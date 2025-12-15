package com.example.UtioyV1.utio;

import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.LogInfo.LogEntryModel;
import com.example.UtioyV1.utio.LogInfo.LogLevel;
import com.example.UtioyV1.utio.LogInfo.LogQueue;
import com.example.UtioyV1.utio.LogInfo.LogWriter;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 日志功能主类
 * 提供日志记录的统一入口
 */
public class Log {

    // 批量写入配置（从配置文件读取，默认值作为后备）
    private static int getBatchSize() {
        return Config.LOG_BATCH_SIZE > 0 ? Config.LOG_BATCH_SIZE : 100;
    }

    private static long getFlushIntervalMs() {
        return Config.LOG_FLUSH_INTERVAL_SECONDS > 0 ? Config.LOG_FLUSH_INTERVAL_SECONDS * 1000 : 2000;
    }

    // 写入线程数量（可根据CPU核心数调整）
//    private static final int WRITER_THREAD_COUNT = Math.max(2, Runtime.getRuntime().availableProcessors() / 2);
    private static final int WRITER_THREAD_COUNT = 1;

    // 后台线程
    private static volatile boolean running = true;
    private static Thread[] logThreads;


//
    // 初始化日志线程
     static {
        initializeLogThread();
    }

    /**
     * 初始化日志处理线程（多线程版本）
     */



    private  static void initializeLogThread(){
        // 如果日志未启用，不启动线程
        if (!Config.LOG_ENABLED) {
            return;
        }

        // 初始化日志目录
        LogWriter.initializeLogDir();

        // 如果文件输出未启用，不启动写入线程
        if (!Config.LOG_FILE_OUTPUT) {
            return;
        }



        // 启动多个后台写入线程（提高并发处理能力）
        logThreads = new Thread[WRITER_THREAD_COUNT];
        for (int i = 0; i < WRITER_THREAD_COUNT; i++) {
            System.out.println("启动线程");
            final int threadId = i;
            logThreads[i] = new Thread(() -> {
                int batchSize = getBatchSize();
                long flushIntervalMs = getFlushIntervalMs();
                List<LogEntryModel> batch = new ArrayList<>(batchSize);
                long lastFlushTime = System.currentTimeMillis();

                while (running || !LogQueue.isEmpty()) {
                    try {
                        // 批量收集日志（使用drainTo提高效率）
                        int drained = LogQueue.drainTo(batch, batchSize);
                        if (drained == 0) {
                            // 如果队列为空，等待一段时间
                            LogEntryModel entry = LogQueue.poll(1000, TimeUnit.MILLISECONDS);
                            if (entry != null) {
                                batch.add(entry);
                            }
                        }
//                        System.out.println("启动");

                        long currentTime = System.currentTimeMillis();
                        boolean shouldFlush = batch.size() >= batchSize ||
                                             (currentTime - lastFlushTime >= flushIntervalMs && !batch.isEmpty()) ||
                                             (!running && LogQueue.isEmpty() && !batch.isEmpty());

                        if (shouldFlush && !batch.isEmpty()) {
                            LogWriter.writeBatchToFile(batch);
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
                    LogWriter.writeBatchToFile(batch);
                }
            }, "LogWriterThread-" + threadId);

            logThreads[i].setDaemon(true);
            logThreads[i].start();
        }

        // 注册JVM关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(Log::shutdown));
    }

    /**
     * 关闭日志系统
     */
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
        List<LogEntryModel> remaining = new ArrayList<>();
        LogQueue.drainTo(remaining);
        if (!remaining.isEmpty()) {
            LogWriter.writeBatchToFile(remaining);
        }

        LogWriter.closeAllWriters();
    }

    /**
     * 添加日志到队列（带type参数）
     */


    private static void addLog(LogEntryModel log) {

//        LogLevel level, String msg, String type
        // 如果日志未启用，直接返回
        if (!Config.LOG_ENABLED) {
            return;
        }
        
        // 检查该日志级别是否启用
        Boolean levelEnabled = Config.LOG_LEVELS_ENABLED.get(log.getLevel().getValue());
        if (levelEnabled == null || !levelEnabled) {
            return; // 该级别未启用，直接返回
        }

        // 根据配置决定是否输出到控制台
        if (Config.LOG_CONSOLE_OUTPUT) {
            if (log.getLevel() == LogLevel.ERROR || log.getLevel() == LogLevel.SEVERE) {
                System.err.println(log.getMessage());
            } else {
                System.out.println(log.getMessage());
            }
        }

        // 如果文件输出未启用，不添加到队列
        if (!Config.LOG_FILE_OUTPUT) {
            return;
        }

        // 添加到队列（非阻塞，如果队列满则丢弃）
        if (!LogQueue.offer(log)) {
            // 队列满时的处理：记录警告并丢弃
            if (Config.LOG_CONSOLE_OUTPUT) {
                System.err.println("WARNING: Log queue is full (" + LogQueue.size() + "), dropping log: " +
                                 (log.getMessage().length() > 100 ? log.getMessage().substring(0, 100) + "..." : log.getMessage()));
            }
        }
    }


    /**
     * 获取当前队列大小（用于监控）
     */
    public static int getQueueSize() {
        return LogQueue.size();
    }

    /**
     * 获取队列剩余容量
     */
    public static int getRemainingCapacity() {
        return LogQueue.remainingCapacity();
    }

    // ========== 公共API方法 ==========

    /**
     * 记录INFO级别日志（带type参数）
     */
    public static void info(String msg, String type) {
        addLog(new LogEntryModel(LogLevel.INFO,msg,type));
    }

    /**
     * 记录ERROR级别日志（带type参数）
     */
    public static void error(String msg, String type) {
        addLog(new LogEntryModel(LogLevel.ERROR,msg,type));
    }

    /**
     * 记录DEBUG级别日志（带type参数）
     */
    public static void debug(String msg, String type) {
        addLog(new LogEntryModel(LogLevel.DEBUG,msg,type));
    }

    /**
     * 记录INFO级别日志（不带type参数，向后兼容）
     */
    public static void info(String msg) {
        addLog(new LogEntryModel(LogLevel.INFO,msg,"info"));
    }

    /**
     * 记录ERROR级别日志（不带type参数，向后兼容）
     */
    public static void error(String msg) {
        addLog(new LogEntryModel(LogLevel.INFO,msg,"error"));

    }

    /**
     * 记录DEBUG级别日志（不带type参数，向后兼容）
     */
    public static void debug(String msg) {
        addLog(new LogEntryModel(LogLevel.INFO,msg,"debug"));
    }

    /**
     * 记录WARN级别日志（带type参数）
     */
    public static void warn(String msg, String type) {
        addLog(new LogEntryModel(LogLevel.INFO,msg,type));
    }

    /**
     * 记录WARN级别日志（不带type参数）
     */
    public static void warn(String msg) {
        addLog(new LogEntryModel(LogLevel.INFO,msg,"warn"));


    }

    /**
     * 记录SEVERE级别日志（严重警告，带type参数）
     */
    public static void severe(String msg, String type) {
        addLog(new LogEntryModel(LogLevel.INFO,msg,type));

    }

    /**
     * 记录SEVERE级别日志（严重警告，不带type参数）
     */
    public static void severe(String msg) {
        addLog(new LogEntryModel(LogLevel.INFO,msg,"severe"));

    }



    /**
     * 记录数据库日志
     */
    public static void dao_save(String type,String msg,String user) {
        LogEntryModel daoSave = new LogEntryModel(LogLevel.DAO, msg, type,user);
        addLog(daoSave);
        LogQueue.dao_list.add(daoSave);

    }

}
