package com.example.UtioyV1;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.example.*.mapper") //扫描com.example.UtioyV1.mapper下的依赖
public class SpringUtioYV1Application {

    public static void main(String[] args) {




        SpringApplication.run(SpringUtioYV1Application.class, args);

    }

}