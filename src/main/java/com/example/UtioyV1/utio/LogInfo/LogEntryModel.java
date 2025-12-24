package com.example.UtioyV1.utio.LogInfo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 日志条目类
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
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
     * 预留字段
     */
    private String reserved;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 创建id
     */
    private Long create_id;







    
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

    public LogEntryModel(LogLevel level, String message, String type,String user,String reserved) {
        this.level = level;
        this.message = message;
        this.type = type;
        this.create_time = new Date();
        this.user = user;
        this.reserved = reserved;

    }

    public LogEntryModel() {
    }
}