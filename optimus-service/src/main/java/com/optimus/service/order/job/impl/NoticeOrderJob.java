package com.optimus.service.order.job.impl;

import java.util.List;
import java.util.Map;

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
 * 订单通知
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class NoticeOrderJob extends BaseOrderJob {

    /** 回调商户次数系统配置项 */
    private static final String MERCHANT_CALLBACK_COUNT_BASE_KEY = "MERCHANT_CALLBACK_COUNT";

    /** 回调商户次数默认值 */
    private static final Short MERCHANT_CALLBACK_COUNT = 3;

    @Autowired
    private OrderManager orderManager;

    @Resource
    private OrderInfoDao orderInfoDao;

    @Scheduled(initialDelay = 60000, fixedDelay = 60000)
    @Override
    public void execute() {

        // 分片
        Map<Integer, Integer> shardingMap = super.sharding(this.getClass().getSimpleName());
        if (CollectionUtils.isEmpty(shardingMap)) {
            return;
        }

        // 回调商户次数
        Short merchantCallbackCount = MERCHANT_CALLBACK_COUNT;
        String value = super.loadSystemConfig(MERCHANT_CALLBACK_COUNT_BASE_KEY);
        if (StringUtils.hasLength(value)) {
            merchantCallbackCount = Short.parseShort(value);
        }

        // 订单信息Query
        OrderInfoQuery query = new OrderInfoQuery();
        query.setLastTime(DateUtil.currentDate());
        query.setMerchantCallbackCount(merchantCallbackCount);
        query.setMerchantNotifyStatus(OrderMerchantNotifyStatusEnum.MERCHANT_NOTIFY_STATUS_NS.getCode());
        query.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AP.getCode());
        query.setOrderType(OrderTypeEnum.ORDER_TYPE_C.getCode());
        query.setPage(new Page(1, 1000));

        while (true) {

            // 查询订单
            List<OrderInfoDO> orderInfoList = orderInfoDao.listOrderInfoByOrderInfoQuerys(query);
            if (CollectionUtils.isEmpty(orderInfoList)) {
                break;
            }

            for (OrderInfoDO item : orderInfoList) {

                try {

                    // 订单信息
                    OrderInfoDTO orderInfo = new OrderInfoDTO();
                    BeanUtils.copyProperties(item, orderInfo);

                    // 订单通知
                    boolean result = orderManager.orderNotice(orderInfo);
                    if (!result) {

                        // 失败逻辑

                    }

                } catch (OptimusException e) {
                    log.error("订单通知异常:[{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo());
                    continue;
                } catch (Exception e) {
                    log.error("订单通知异常:{}", e);
                    continue;
                }

            }

        }

    }

}
