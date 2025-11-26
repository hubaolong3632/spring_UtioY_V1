package com.example.spring_utioy_v1.utio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务执行类（使用配置的周期）
 */
@Configuration
@EnableScheduling
public class TimeConfig {

    /**
     * 定时任务方法：每隔配置的周期6500执行一次
     * 单位：毫秒（需将配置的秒转换为毫秒）
     */
//    @Scheduled(fixedRateString = "6500000") // 秒 -> 毫秒（拼接 "000"）
//    public void executeTask() {
//
//
//    }




}