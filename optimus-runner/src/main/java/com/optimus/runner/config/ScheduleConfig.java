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

    private static final String THREAD_NAME_PREFIX = "optimus-job-";

    private static final int POOL_SIZE = 10;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();

        scheduler.setThreadNamePrefix(THREAD_NAME_PREFIX);
        scheduler.setPoolSize(POOL_SIZE);

        scheduler.initialize();

        taskRegistrar.setScheduler(scheduler);

    }

}
