package com.optimus.runner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * ScheduleConfig
 * 
 * @author sunxp
 */
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {

    /** 线程名称前缀 */
    private static final String THREAD_NAME_PREFIX = "optimus-job-";

    /** 线程数 */
    private static final int POOL_SIZE = 10;

    /**
     * 定时任务线程池
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setThreadNamePrefix(THREAD_NAME_PREFIX);
        scheduler.setPoolSize(POOL_SIZE);

        scheduler.initialize();

        taskRegistrar.setScheduler(scheduler);

    }

}
