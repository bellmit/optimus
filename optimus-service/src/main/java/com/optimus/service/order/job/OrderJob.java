package com.optimus.service.order.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单定时任务
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class OrderJob {

    @Scheduled(initialDelay = 60000, fixedDelay = 120000)
    public void orderQueryTask() {

        log.info("orderQueryTask start...");

    }

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void orderNoticeTask() {

        log.info("orderNoticeTask start...");

    }

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void orderReleaseTask() {

        log.info("orderReleaseTask start...");

    }

    @Scheduled(initialDelay = 60000, fixedDelay = 10000)
    public void orderSplitProfitTask() {

        log.info("orderSplitProfitTask start...");

    }

}
