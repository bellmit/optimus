package com.optimus.service.order.job.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import com.optimus.dao.domain.GatewaySubChannelDO;
import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.GatewaySubChannelDao;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.dao.query.OrderInfoQuery;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.service.order.job.BaseOrderJob;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.common.CommonSystemConfigEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.model.page.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 渠道订单查询
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class ChannelOrderQueryJob extends BaseOrderJob {

    /** 渠道订单查询次数默认值 */
    private static Short channelOrderQueryCount = 3;

    /** 渠道订单查询一次执行上限配置项 */
    private static Integer channelOrderQueryOnceExecuteLimit = 100;

    /** 渠道订单查询间隔默认值 */
    private static Integer channelOrderQueryInterval = 10;

    @Autowired
    private GatewayManager gatewayManager;

    @Resource
    private OrderInfoDao orderInfoDao;

    @Resource
    private GatewaySubChannelDao gatewaySubChannelDao;

    @Scheduled(initialDelay = 60000, fixedDelay = 120000)
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

        while (index.compareTo(channelOrderQueryOnceExecuteLimit) < 0) {

            index++;

            // 查询订单
            List<OrderInfoDO> orderInfoList = orderInfoDao.listOrderInfoByOrderInfoQuerys(query);
            if (CollectionUtils.isEmpty(orderInfoList)) {
                break;
            }

            for (OrderInfoDO item : orderInfoList) {

                // 查询网关子渠道
                GatewaySubChannelDO gatewaySubChannel = getGatewaySubChannel(item);
                if (Objects.isNull(gatewaySubChannel)) {
                    continue;
                }

                // 执行脚本
                String orderStatus = executeScript(item, gatewaySubChannel);

                // 更新订单信息
                updateOrderInfo(item, orderStatus);

            }

        }

    }

    /**
     * 初始化
     */
    private void init() {

        // 渠道订单查询次数
        String value0 = super.loadSystemConfig(CommonSystemConfigEnum.CHANNEL_ORDER_QUERY_COUNT.getCode());
        if (StringUtils.hasLength(value0)) {
            channelOrderQueryCount = Short.parseShort(value0);
        }

        // 一次执行上限
        String value1 = super.loadSystemConfig(CommonSystemConfigEnum.CHANNEL_ORDER_QUERY_ONCE_EXECUTE_LIMIT.getCode());
        if (StringUtils.hasLength(value1)) {
            channelOrderQueryOnceExecuteLimit = Integer.parseInt(value1);
        }

        // 间隔
        String value2 = super.loadSystemConfig(CommonSystemConfigEnum.CHANNEL_ORDER_QUERY_INTERVAL.getCode());
        if (StringUtils.hasLength(value2)) {
            channelOrderQueryInterval = Integer.parseInt(value2);
        }

    }

    /**
     * 获取订单信息Query
     * 
     * @return
     */
    private OrderInfoQuery getOrderInfoQuery() {

        // 分片
        Map<Integer, Integer> shardingMap = super.sharding(CommonSystemConfigEnum.CHANNEL_ORDER_QUERY_JOB_SHARDING.getCode());
        if (CollectionUtils.isEmpty(shardingMap)) {
            return null;
        }

        // 订单信息Query
        OrderInfoQuery query = new OrderInfoQuery();
        query.setPage(new Page(1, 1000));
        query.setShard(shardingMap.entrySet().stream().findFirst().get().getKey());
        query.setTotalShard(shardingMap.entrySet().stream().findFirst().get().getValue());
        query.setLastTime(DateUtil.offsetForMinute(DateUtil.currentDate(), -channelOrderQueryInterval));
        query.setOrderType(OrderTypeEnum.ORDER_TYPE_C.getCode());
        query.setOrderStatus(OrderStatusEnum.ORDER_STATUS_NP.getCode());
        query.setChannelOrderQueryCount(channelOrderQueryCount);

        return query;

    }

    /**
     * 查询网关子渠道
     * 
     * @param orderInfo
     * @return
     */
    private GatewaySubChannelDO getGatewaySubChannel(OrderInfoDO orderInfo) {

        try {

            // 查询网关子渠道
            return gatewaySubChannelDao.getGatewaySubChannelBySubChannelCode(orderInfo.getSubChannelCode());

        } catch (Exception e) {
            log.error("渠道订单查询网关子渠道异常:", e);
            return null;
        }

    }

    /**
     * 执行脚本
     * 
     * @param orderInfo
     * @param gatewaySubChannel
     * @return
     */
    private String executeScript(OrderInfoDO orderInfo, GatewaySubChannelDO gatewaySubChannel) {

        // 执行脚本输入
        ExecuteScriptInputDTO input = OrderManagerConvert.getExecuteScriptInputDTO(orderInfo, gatewaySubChannel);
        if (Objects.isNull(input)) {
            return null;
        }

        try {

            // 执行脚本
            ExecuteScriptOutputDTO output = gatewayManager.executeScript(input);
            if (Objects.isNull(output)) {
                return null;
            }

            // 明确成功或不成功
            if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AP.getCode(), output.getOrderStatus()) && !StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AF.getCode(), output.getOrderStatus())) {
                return null;
            }

            return output.getOrderStatus();

        } catch (Exception e) {
            log.error("渠道订单查询执行脚本异常:", e);
            return null;
        }

    }

    /**
     * 更新订单信息
     * 
     * @param orderInfo
     * @param orderStatus
     */
    private void updateOrderInfo(OrderInfoDO orderInfo, String orderStatus) {

        try {

            // 明确成功或不成功
            if (StringUtils.hasLength(orderStatus)) {
                orderInfoDao.updateOrderInfoByOrderIdAndOrderStatus(orderInfo.getOrderId(), orderStatus, OrderStatusEnum.ORDER_STATUS_NP.getCode(), DateUtil.currentDate());
                return;
            }

            // 未知订单状态
            OrderInfoDO orderInfoDO = new OrderInfoDO();
            orderInfoDO.setId(orderInfo.getId());
            orderInfoDO.setChannelOrderQueryCount(channelOrderQueryCount);
            orderInfoDO.setUpdateTime(DateUtil.currentDate());

            orderInfoDao.updateOrderInfoForChannelOrderQuery(orderInfoDO);

        } catch (Exception e) {
            log.error("渠道订单查询更新订单信息异常:", e);
        }

    }

}
