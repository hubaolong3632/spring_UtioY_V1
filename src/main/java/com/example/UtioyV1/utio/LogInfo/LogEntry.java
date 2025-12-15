package com.example.UtioyV1.utio.LogInfo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * 日志条目类
 */
@Data
public class LogEntry {

    /**
     * 主键id
     */
    private Long id;


    /**
     * 日志等级
     */
    private  LogLevel level;
    private  String message;

    /**
     * 日志类别
     */
    private  String type;

    /**
     * 操作用户
     */
    private  String user;


    /**
     * 内容
     */
    private String content;


    /**
     * 创建时间
     */
    private Date create_time;




    
    public LogEntry(LogLevel level, String message, String type) {
        this.level = level;
        this.message = message;
        this.type = type;
        this.create_time = new Date();
    }

    public LogEntry() {
    }
}

