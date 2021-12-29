package com.optimus.service.order.job.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.dao.query.OrderInfoQuery;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.service.order.job.BaseOrderJob;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.common.CommonSystemConfigEnum;
import com.optimus.util.constants.order.OrderMerchantNotifyStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.util.model.page.Page;

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

    /** 回调商户次数默认值 */
    private static Short merchantCallbackCount = 3;

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

        while (true) {

            index++;

            // 设置分页对象
            query.getPage().setPageNo(index);

            // 查询订单
            List<OrderInfoDO> orderInfoList = orderInfoDao.listOrderInfoForJobByOrderInfoQuerys(query);
            if (CollectionUtils.isEmpty(orderInfoList)) {
                break;
            }

            for (OrderInfoDO item : orderInfoList) {

                // 订单信息
                OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(item);

                // 回调商户:订单通知
                boolean noticeResult = noticeOrder(orderInfo);

                // 更新订单信息
                updateOrderInfo(item.getId(), noticeResult);

            }

        }

    }

    /**
     * 初始化
     */
    private void init() {

        // 回调商户次数
        String value0 = super.loadSystemConfig(CommonSystemConfigEnum.MERCHANT_CALLBACK_COUNT.getCode());
        if (StringUtils.hasLength(value0)) {
            merchantCallbackCount = Short.parseShort(value0);
        }

        // 间隔
        String value1 = super.loadSystemConfig(CommonSystemConfigEnum.MERCHANT_CALLBACK_INTERVAL.getCode());
        if (StringUtils.hasLength(value1)) {
            merchantCallbackInterval = Integer.parseInt(value1);
        }

    }

    /**
     * 获取订单信息Query
     * 
     * @return
     */
    private OrderInfoQuery getOrderInfoQuery() {

        // 分片
        Map<Integer, Integer> shardingMap = super.sharding(CommonSystemConfigEnum.MERCHANT_CALLBACK_JOB_SHARDING.getCode());
        if (CollectionUtils.isEmpty(shardingMap)) {
            return null;
        }

        // 订单信息Query
        OrderInfoQuery query = new OrderInfoQuery();
        query.setPage(new Page(1, BaseOrderJob.BASE_ORDER_JOB_PAGE_SIZE));
        query.setShard(shardingMap.entrySet().stream().findFirst().get().getKey());
        query.setTotalShard(shardingMap.entrySet().stream().findFirst().get().getValue());
        query.setOrderType(OrderTypeEnum.ORDER_TYPE_C.getCode());
        query.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AP.getCode());
        query.setMerchantNotifyStatus(OrderMerchantNotifyStatusEnum.MERCHANT_NOTIFY_STATUS_NS.getCode());
        query.setMerchantCallbackCount(merchantCallbackCount);
        query.setPayTime(DateUtil.offsetForMinute(DateUtil.currentDate(), -merchantCallbackInterval));

        return query;

    }

    /**
     * 订单通知
     * 
     * @param orderInfo
     * @return
     */
    private boolean noticeOrder(OrderInfoDTO orderInfo) {

        try {

            // 订单通知
            return orderManager.orderNotice(orderInfo);

        } catch (OptimusException e) {
            log.warn("订单通知异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
            return false;
        } catch (Exception e) {
            log.error("订单通知异常:", e);
            return false;
        }

    }

    /**
     * 更新订单信息
     * 
     * @param id
     * @param noticeResult
     */
    private void updateOrderInfo(Long id, boolean noticeResult) {

        // 订单通知成功
        if (noticeResult) {
            return;
        }

        try {

            // 订单通知失败

            OrderInfoDO orderInfoDO = new OrderInfoDO();
            orderInfoDO.setId(id);
            orderInfoDO.setMerchantNotifyStatus(OrderMerchantNotifyStatusEnum.MERCHANT_NOTIFY_STATUS_NF.getCode());
            orderInfoDO.setMerchantCallbackCount(merchantCallbackCount);
            orderInfoDO.setUpdateTime(DateUtil.currentDate());

            orderInfoDao.updateOrderInfoForMerchantCallback(orderInfoDO);

        } catch (Exception e) {
            log.error("订单通知失败更新记录异常:", e);
        }

    }

}
