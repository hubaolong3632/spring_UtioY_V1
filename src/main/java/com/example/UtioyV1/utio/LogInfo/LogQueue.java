package com.example.UtioyV1.utio.LogInfo;

import java.util.ArrayList;
import java.util.Collections;
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
    private static final BlockingQueue<LogEntryModel> logQueue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);



//    底并发保存的加锁保证读写唯一
    public static List<LogEntryModel> dao_list = Collections.synchronizedList(new ArrayList<>(500));


    /**
     * 添加日志条目到队列
     */
    public static boolean offer(LogEntryModel entry) {
        return logQueue.offer(entry);
    }
    
    /**
     * 从队列中取出日志条目（带超时）
     */
    public static LogEntryModel poll(long timeout, TimeUnit unit) throws InterruptedException {
        return logQueue.poll(timeout, unit);
    }
    
    /**
     * 批量取出日志条目
     */
    public static int drainTo(List<LogEntryModel> list, int maxElements) {
        return logQueue.drainTo(list, maxElements);
    }
    
    /**
     * 取出所有剩余的日志条目
     */
    public static int drainTo(List<LogEntryModel> list) {
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