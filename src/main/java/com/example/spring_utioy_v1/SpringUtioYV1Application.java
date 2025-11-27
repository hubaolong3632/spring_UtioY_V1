package com.example.spring_utioy_v1;

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
        SpringApplication.run(SpringUtioYV1Application.class, args);
    }

}
