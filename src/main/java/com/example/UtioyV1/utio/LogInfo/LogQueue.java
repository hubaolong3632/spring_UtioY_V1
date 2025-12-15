package com.example.UtioyV1.utio.LogInfo;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 日志队列管理类
 */
public class LogQueue {
    
    // 日志队列（限制大小，防止内存溢出）
    private static final int MAX_QUEUE_SIZE = 100000; // 最大队列大小
    private static final BlockingQueue<LogEntry> logQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);
    
    /**
     * 添加日志条目到队列
     */
    public static boolean offer(LogEntry entry) {
        return logQueue.offer(entry);
    }
    
    /**
     * 从队列中取出日志条目（带超时）
     */
    public static LogEntry poll(long timeout, TimeUnit unit) throws InterruptedException {
        return logQueue.poll(timeout, unit);
    }
    
    /**
     * 批量取出日志条目
     */
    public static int drainTo(List<LogEntry> list, int maxElements) {
        return logQueue.drainTo(list, maxElements);
    }
    
    /**
     * 取出所有剩余的日志条目
     */
    public static int drainTo(List<LogEntry> list) {
        return logQueue.drainTo(list);
    }
    
    /**
     * 检查队列是否为空
     */
    public static boolean isEmpty() {
        return logQueue.isEmpty();
    }
    
    /**
     * 获取当前队列大小
     */
    public static int size() {
        return logQueue.size();
    }
    
    /**
     * 获取队列剩余容量
     */
    public static int remainingCapacity() {
        return logQueue.remainingCapacity();
    }
}

