package com.optimus.runner.config;

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

    private static final int MAX_POOL_SIZE = 100;

    private static final int CORE_POOL_SIZE = 50;

    private static final String THREAD_NAME_PREFIX = "async-task-thread-pool-";

    @Bean("asyncTaskExecutor")
    public AsyncTaskExecutor asyncTaskExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);

        executor.initialize();

        return executor;

    }

}
