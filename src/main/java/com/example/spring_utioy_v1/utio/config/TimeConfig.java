package com.example.spring_utioy_v1.utio.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

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


//    /**
//     * 例：开始启动执行一次后续每30分钟执行一次
//     * Cron 语法：秒 分 时 日 月 周 年（年可选）
//     */
//    @PostConstruct // 启动后立即执行一次
//    @Scheduled(cron = "0 */30 * * * ?")
//    public void cronTask() {
//        System.out.println("30分钟执行一次");
//    }





}