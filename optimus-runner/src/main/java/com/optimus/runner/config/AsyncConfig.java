package com.optimus.runner.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * AsyncConfig
 * 
 * @author sunxp
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    /** 线程名称前缀 */
    private static final String THREAD_NAME_PREFIX = "optimus-async-";

    /** 最大线程数 */
    private static final int MAX_POOL_SIZE = 20;

    /** 核心线程数 */
    private static final int CORE_POOL_SIZE = 5;

    /** 任务队列 */
    private static final int QUEUE_CAPACITY = 1000;

    /** 缓冲队列中线程的空闲时间 */
    private static final int KEEP_ALIVE_SECONDS = 100;

    @Bean("asyncTaskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);

        executor.initialize();

        return executor;

    }

}
