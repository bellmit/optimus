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
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.service.order.job.BaseOrderJob;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.common.CommonSystemConfigEnum;
import com.optimus.util.constants.order.OrderSplitProfitStatusEnum;
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

    /** 渠道订单查询间隔默认值 */
    private static Integer channelOrderQueryInterval = 10;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private GatewayManager gatewayManager;

    @Resource
    private OrderInfoDao orderInfoDao;

    @Resource
    private GatewaySubChannelDao gatewaySubChannelDao;

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

                // 查询网关子渠道
                GatewaySubChannelDO gatewaySubChannel = getGatewaySubChannel(item);

                // 执行脚本
                String orderStatus = executeScript(item, gatewaySubChannel);

                // 获取订单信息DTO
                OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(item);
                orderInfo.setOrderStatus(orderStatus);

                // 处理订单信息
                handleOrderInfo(orderInfo);

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

        // 间隔
        String value1 = super.loadSystemConfig(CommonSystemConfigEnum.CHANNEL_ORDER_QUERY_INTERVAL.getCode());
        if (StringUtils.hasLength(value1)) {
            channelOrderQueryInterval = Integer.parseInt(value1);
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
        query.setPage(new Page(1, BaseOrderJob.BASE_ORDER_JOB_PAGE_SIZE));
        query.setShard(shardingMap.entrySet().stream().findFirst().get().getKey());
        query.setTotalShard(shardingMap.entrySet().stream().findFirst().get().getValue());
        query.setOrderType(OrderTypeEnum.ORDER_TYPE_C.getCode());
        query.setOrderStatus(OrderStatusEnum.ORDER_STATUS_NP.getCode());
        query.setChannelOrderQueryCount(channelOrderQueryCount);
        query.setCreateTime(DateUtil.offsetForMinute(DateUtil.currentDate(), -channelOrderQueryInterval));

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

        try {

            // 执行脚本输入
            ExecuteScriptInputDTO input = OrderManagerConvert.getExecuteScriptInputDTO(orderInfo, gatewaySubChannel);
            if (Objects.isNull(input)) {
                return null;
            }

            // 执行脚本
            ExecuteScriptOutputDTO output = gatewayManager.executeScript(input);
            if (Objects.isNull(output)) {
                return null;
            }

            return output.getOrderStatus();

        } catch (Exception e) {
            log.error("渠道订单查询执行脚本异常:", e);
            return null;
        }

    }

    /**
     * 处理订单信息
     * 
     * @param orderInfo
     */
    private void handleOrderInfo(OrderInfoDTO orderInfo) {

        try {

            // 订单信息DO
            OrderInfoDO orderInfoDO = new OrderInfoDO();
            orderInfoDO.setId(orderInfo.getId());
            orderInfoDO.setUpdateTime(DateUtil.currentDate());

            // 非明确成功或不成功
            if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AP.getCode(), orderInfo.getOrderStatus()) && !StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AF.getCode(), orderInfo.getOrderStatus())) {

                orderInfoDO.setChannelOrderQueryCount(channelOrderQueryCount);
                orderInfoDao.updateOrderInfoForChannelOrderQuery(orderInfoDO);

                return;
            }

            // 明确成功或不成功
            orderInfoDO.setOrderStatus(orderInfo.getOrderStatus());
            orderInfoDO.setBehavior(orderInfo.getBehavior());

            if (StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AP.getCode(), orderInfo.getOrderStatus())) {
                orderInfoDO.setSplitProfitStatus(OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_N.getCode());
                orderInfoDO.setPayTime(DateUtil.currentDate());
            }

            int update = orderInfoDao.updateOrderInfoByIdAndOrderStatus(orderInfoDO, OrderStatusEnum.ORDER_STATUS_NP.getCode());
            if (update != 1) {
                return;
            }

            // 异步释放
            orderManager.asyncRelease(orderInfo);

            // 异步分润
            orderManager.asyncSplitProfit(orderInfo);

            // 异步订单通知
            orderManager.asyncOrderNotice(orderInfo);

        } catch (Exception e) {
            log.error("渠道订单查询处理订单信息异常:", e);
        }

    }

}
