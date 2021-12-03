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
import com.optimus.util.constants.order.OrderMerchantNotifyStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
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
 * 回调商户
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class MerchantCallbackJob extends BaseOrderJob {

    /** 回调商户定时任务分片配置项 */
    private static final String MERCHANT_CALLBACK_JOB_SHARDING_BASE_KEY = "MERCHANT_CALLBACK_JOB_SHARDING";

    /** 回调商户次数系统配置项 */
    private static final String MERCHANT_CALLBACK_COUNT_BASE_KEY = "MERCHANT_CALLBACK_COUNT";

    /** 回调商户一次执行上限配置项 */
    private static final String MERCHANT_CALLBACK_ONCE_EXECUTE_LIMIT_BASE_KEY = "MERCHANT_CALLBACK_ONCE_EXECUTE_LIMIT";

    /** 回调商户间隔配置项 */
    private static final String MERCHANT_CALLBACK_INTERVAL_BASE_KEY = "MERCHANT_CALLBACK_INTERVAL";

    /** 回调商户次数默认值 */
    private static Short merchantCallbackCount = 3;

    /** 回调商户一次执行上限默认值 */
    private static Integer merchantCallbackOnceExecuteLimit = 100;

    /** 回调商户间隔默认值 */
    private static Integer merchantCallbackInterval = 10;

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

        while (index.compareTo(merchantCallbackOnceExecuteLimit) < 0) {

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

                // 回调商户:订单通知
                noticeOrder(orderInfo);

            }

        }

    }

    /**
     * 初始化
     */
    private void init() {

        // 回调商户次数
        String value0 = super.loadSystemConfig(MERCHANT_CALLBACK_COUNT_BASE_KEY);
        if (StringUtils.hasLength(value0)) {
            merchantCallbackCount = Short.parseShort(value0);
        }

        // 一次执行上限
        String value1 = super.loadSystemConfig(MERCHANT_CALLBACK_ONCE_EXECUTE_LIMIT_BASE_KEY);
        if (StringUtils.hasLength(value1)) {
            merchantCallbackOnceExecuteLimit = Integer.parseInt(value1);
        }

        // 间隔
        String value2 = super.loadSystemConfig(MERCHANT_CALLBACK_INTERVAL_BASE_KEY);
        if (StringUtils.hasLength(value2)) {
            merchantCallbackInterval = Integer.parseInt(value2);
        }

    }

    /**
     * 获取订单信息Query
     * 
     * @return
     */
    private OrderInfoQuery getOrderInfoQuery() {

        // 分片
        Map<Integer, Integer> shardingMap = super.sharding(MERCHANT_CALLBACK_JOB_SHARDING_BASE_KEY);
        if (CollectionUtils.isEmpty(shardingMap)) {
            return null;
        }

        // 订单信息Query
        OrderInfoQuery query = new OrderInfoQuery();
        query.setPage(new Page(1, 1000));
        query.setShard(shardingMap.entrySet().stream().findFirst().get().getKey());
        query.setTotalShard(shardingMap.entrySet().stream().findFirst().get().getValue());
        query.setLastTime(DateUtil.offsetForMinute(DateUtil.currentDate(), -merchantCallbackInterval));
        query.setOrderType(OrderTypeEnum.ORDER_TYPE_C.getCode());
        query.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AP.getCode());
        query.setMerchantNotifyStatus(OrderMerchantNotifyStatusEnum.MERCHANT_NOTIFY_STATUS_NS.getCode());
        query.setMerchantCallbackCount(merchantCallbackCount);

        return query;

    }

    /**
     * 订单通知
     * 
     * @param orderInfo
     */
    private void noticeOrder(OrderInfoDTO orderInfo) {

        // 订单通知结果
        boolean result = false;

        try {

            // 订单通知
            result = orderManager.orderNotice(orderInfo);

        } catch (OptimusException e) {
            log.error("订单通知异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
        } catch (Exception e) {
            log.error("订单通知异常:", e);
        }

        // 通知成功
        if (result) {
            return;
        }

        // 通知失败
        try {

            OrderInfoDO orderInfoDO = new OrderInfoDO();
            orderInfoDO.setMerchantNotifyStatus(OrderMerchantNotifyStatusEnum.MERCHANT_NOTIFY_STATUS_NF.getCode());
            orderInfoDO.setMerchantCallbackCount(merchantCallbackCount);
            orderInfoDO.setUpdateTime(DateUtil.currentDate());

            orderInfoDao.updateOrderInfoForMerchantCallback(orderInfoDO);

        } catch (Exception e) {
            log.error("订单通知失败更新记录异常:", e);
        }

    }

}
