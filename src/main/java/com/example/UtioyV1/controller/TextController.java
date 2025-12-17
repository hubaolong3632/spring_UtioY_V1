package com.example.UtioyV1.controller;

import com.example.UtioyV1.utio.Code.Config;
import com.example.UtioyV1.utio.Log;
import com.example.UtioyV1.utio.mapper.UtioMapper;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TextController {

    @RequestMapping("/v1")
    public String text1() throws InterruptedException {
//        for(int i=0;i<10;i++){
//            Log.error("100000");
//        }

        String pz = Config.getDaoValue("配置");
        System.out.println(pz);

        Log.dao_save("数据库错误","text1");

        return "hello world";
    }
}