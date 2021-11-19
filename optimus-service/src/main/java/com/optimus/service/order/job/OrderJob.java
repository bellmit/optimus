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
    public void queryOrder() {

        log.info("queryOrder start...");

    }

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void noticeMerchant() {

        log.info("noticeMerchant start...");

    }

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    public void pendingOrder() {

        log.info("pendingOrder start...");

    }

    @Scheduled(initialDelay = 60000, fixedDelay = 10000)
    public void splitProfit() {

        log.info("splitProfit start...");

    }

}
