package com.optimus.service.order.job.impl;

import java.util.Map;

import com.optimus.service.order.job.BaseOrderJob;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 释放订单
 * 
 * @author sunxp
 */
@Component
public class ReleaseOrderJob extends BaseOrderJob {

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    @Override
    public void execute() {

        // 分片
        Map<Integer, Integer> shardingMap = super.sharding(this.getClass().getSimpleName());
        if (CollectionUtils.isEmpty(shardingMap)) {
            return;
        }

    }

}