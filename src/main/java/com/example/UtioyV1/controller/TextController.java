package com.example.UtioyV1.controller;

import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.LogInfo.LogEntryModel;
import com.example.UtioyV1.utio.LogInfo.LogLevel;
import com.example.UtioyV1.utio.mapper.UtioMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TextController {

@Resource
private UtioMapper ui;

    @RequestMapping("/v1")
    public String text1() throws InterruptedException {
        List<LogEntryModel> logEntries=new ArrayList<>();
        LogEntryModel sysLog = new LogEntryModel();
        sysLog.setType("type");
        sysLog.setUser("张三");
        sysLog.setLevel(LogLevel.INFO);

        logEntries.add(sysLog);
        logEntries.add(sysLog);
        ui.save_log(logEntries);

//        System.out.println("启动线程"+  Thread.currentThread().getName());

        for(int i=0;i<10;i++){
            Log.error("100000");
//            Thread.sleep(10);
        }


        return "hello world";
    }
}
