package com.example.UtioyV1;

import com.example.UtioyV1.utio.Log;
import lombok.Value;
import org.apache.ibatis.mapping.Environment;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.example.*.mapper") //扫描com.example.UtioyV1.mapper下的依赖
public class SpringUtioYV1Application {

    public static void main(String[] args) {
     SpringApplication.run(SpringUtioYV1Application.class, args);
    }

}