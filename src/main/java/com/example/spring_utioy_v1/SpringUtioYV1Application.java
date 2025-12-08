package com.example.spring_utioy_v1;

import com.example.spring_utioy_v1.utio.Log;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling //自动注入yml
@EnableAsync // 开启异步支持
@MapperScan("com.example.spring_utioy_v1.mapper")
public class SpringUtioYV1Application {

    public static void main(String[] args) {

        System.out.println("ssss");
        Log.info("xxxx");
//        Log.info("xxxx",);
//        Log.infoSave();


        SpringApplication.run(SpringUtioYV1Application.class, args);
    }

}
