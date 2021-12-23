package com.optimus.service.order.job.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.dao.query.OrderInfoQuery;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.service.order.job.BaseOrderJob;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.order.OrderReleaseStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.util.model.page.Page;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 释放订单
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class ReleaseOrderJob extends BaseOrderJob {

    /** 释放订单定时任务分片配置项 */
    private static final String RELEASE_ORDER_JOB_SHARDING_BASE_KEY = "RELEASE_ORDER_JOB_SHARDING";

    /** 释放订单一次执行上限配置项 */
    private static final String RELEASE_ORDER_ONCE_EXECUTE_LIMIT_BASE_KEY = "RELEASE_ORDER_ONCE_EXECUTE_LIMIT";

    /** 释放订单间隔配置项 */
    private static final String RELEASE_ORDER_INTERVAL_BASE_KEY = "RELEASE_ORDER_INTERVAL";

    /** 释放订单一次执行上限默认值 */
    private static Integer releaseOrderOnceExecuteLimit = 100;

    /** 释放订单间隔默认值 */
    private static Integer releaseOrderInterval = 10;

    @Autowired
    private OrderManager orderManager;

    @Resource
    private OrderInfoDao orderInfoDao;

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    @Override
    public void execute() {

        // 初始化
        init();

        // 订单信息Query
        OrderInfoQuery query = getOrderInfoQuery();
        if (Objects.isNull(query)) {
            return;
        }

        // 下标
        Integer index = 0;

        while (index.compareTo(releaseOrderOnceExecuteLimit) < 0) {

            index++;

            // 查询订单
            List<OrderInfoDO> orderInfoList = orderInfoDao.listOrderInfoByOrderInfoQuerys(query);
            if (CollectionUtils.isEmpty(orderInfoList)) {
                break;
            }

            for (OrderInfoDO item : orderInfoList) {

                // 订单信息
                OrderInfoDTO orderInfo = new OrderInfoDTO();
                BeanUtils.copyProperties(item, orderInfo);

                try {

                    // 释放订单
                    orderManager.release(orderInfo);

                } catch (OptimusException e) {
                    log.error("释放订单业务异常:", e);
                    log.warn("释放订单异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
                    continue;
                } catch (Exception e) {
                    log.error("释放订单异常:", e);
                    continue;
                }

            }

        }

    }

    /**
     * 初始化
     */
    private void init() {

        // 一次执行上限
        String value1 = super.loadSystemConfig(RELEASE_ORDER_ONCE_EXECUTE_LIMIT_BASE_KEY);
        if (StringUtils.hasLength(value1)) {
            releaseOrderOnceExecuteLimit = Integer.parseInt(value1);
        }

        // 间隔
        String value2 = super.loadSystemConfig(RELEASE_ORDER_INTERVAL_BASE_KEY);
        if (StringUtils.hasLength(value2)) {
            releaseOrderInterval = Integer.parseInt(value2);
        }

    }

    /**
     * 获取订单信息Query
     * 
     * @return
     */
    private OrderInfoQuery getOrderInfoQuery() {

        // 分片
        Map<Integer, Integer> shardingMap = super.sharding(RELEASE_ORDER_JOB_SHARDING_BASE_KEY);
        if (CollectionUtils.isEmpty(shardingMap)) {
            return null;
        }

        // 订单信息Query
        OrderInfoQuery query = new OrderInfoQuery();
        query.setPage(new Page(1, 1000));
        query.setShard(shardingMap.entrySet().stream().findFirst().get().getKey());
        query.setTotalShard(shardingMap.entrySet().stream().findFirst().get().getValue());
        query.setInitTime(DateUtil.offsetForMinute(DateUtil.currentDate(), -releaseOrderInterval));
        query.setOrderType(OrderTypeEnum.ORDER_TYPE_C.getCode());
        query.setReleaseStatus(OrderReleaseStatusEnum.RELEASE_STATUS_N.getCode());

        return query;

    }

}
