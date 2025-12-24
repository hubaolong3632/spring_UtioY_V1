package com.example.UtioyV1.utio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步线程线程池
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);    // 核心线程数
        executor.setMaxPoolSize(20);     // 最大线程数
        executor.setQueueCapacity(1000); // 队列容量
        executor.setThreadNamePrefix("async-");
        return executor;
    }
}
