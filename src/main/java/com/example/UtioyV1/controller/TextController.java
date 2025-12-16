package com.example.UtioyV1.controller;

import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.LogInfo.LogEntryModel;
import com.example.UtioyV1.utio.LogInfo.LogLevel;
import com.example.UtioyV1.utio.mapper.UtioMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TextController {

@Resource
private UtioMapper ui;

    @RequestMapping("/v1")
    public String text1() throws InterruptedException {
        for(int i=0;i<10;i++){
            Log.error("100000");
        }


        Log.dao_save("xxx1","1","login");
        Log.dao_save("xxx1","1","login");


        Log.dao_save("sss",true);

        return "hello world";
    }
}
