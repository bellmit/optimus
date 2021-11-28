package com.optimus.service.order.job.impl;

import java.util.Map;

import com.optimus.service.order.job.BaseOrderJob;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 订单分润
 * 
 * @author sunxp
 */
@Component
public class SplitProfitOrderJob extends BaseOrderJob {

    @Scheduled(initialDelay = 60000, fixedDelay = 10000)
    @Override
    public void execute() {

        // 分片
        Map<Integer, Integer> shardingMap = super.sharding(this.getClass().getSimpleName());
        if (CollectionUtils.isEmpty(shardingMap)) {
            return;
        }

    }

}
