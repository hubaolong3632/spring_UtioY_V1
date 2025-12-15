package com.example.UtioyV1;

import com.example.UtioyV1.utio.Log;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication

@MapperScan("com.example.*.mapper")

public class SpringUtioYV1Application {

    public static void main(String[] args) {

//        System.out.println("ssss");
//        Log.info("xxxx");
//        Log.info("xxxx",);
//        Log.infoSave();


        SpringApplication.run(SpringUtioYV1Application.class, args);
    }

}
