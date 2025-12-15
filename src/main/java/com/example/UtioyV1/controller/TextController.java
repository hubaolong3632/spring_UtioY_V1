package com.example.UtioyV1.controller;

import com.example.UtioyV1.utio.LogInfo.LogEntry;
import com.example.UtioyV1.utio.LogInfo.LogLevel;
import com.example.UtioyV1.utio.mapper.UtioMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TextController {

@Resource
private UtioMapper ui;

    @RequestMapping("/v1")
    public String text1(){
        List<LogEntry> logEntries=new ArrayList<>();
        LogEntry sysLog = new LogEntry();
        sysLog.setContent("xx1");
        sysLog.setType("type");
        sysLog.setUser("张三");
        sysLog.setLevel(LogLevel.INFO);

        logEntries.add(sysLog);
        logEntries.add(sysLog);
        ui.save_log(logEntries);
        return "hello world";
    }
}
