package com.example.UtioyV1.utio.LogInfo;

import lombok.Data;

import java.util.Date;

/**
 * 日志条目类
 */
@Data
public class LogEntryModel {

    /**
     * 主键id
     */
    private Long id;


    /**
     * 日志等级
     */
    private  LogLevel level;

    /**
     * 信息/内容
     */
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
     * 创建时间
     */
    private Date create_time;




    
    public LogEntryModel(LogLevel level, String message, String type) {
        this.level = level;
        this.message = message;
        this.type = type;
        this.create_time = new Date();
    }


    public LogEntryModel(LogLevel level, String message, String type,String user) {
        this.level = level;
        this.message = message;
        this.type = type;
        this.create_time = new Date();
        this.user = user;

    }

    public LogEntryModel() {
    }
}

