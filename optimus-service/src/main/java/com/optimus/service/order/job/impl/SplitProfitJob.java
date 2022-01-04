package com.optimus.service.order.job.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.MemberChannelDao;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.dao.query.OrderInfoQuery;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.service.order.job.BaseOrderJob;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.common.CommonSystemConfigBaseKeyEnum;
import com.optimus.util.constants.order.OrderSplitProfitStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.model.exception.OptimusException;
import com.optimus.util.model.page.Page;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单分润
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class SplitProfitJob extends BaseOrderJob {

    /** 订单分润间隔默认值 */
    private static Integer splitProfitInterval = 10;

    @Autowired
    private OrderManager orderManager;

    @Resource
    private MemberChannelDao memberChannelDao;

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
                OrderInfoDTO orderInfo = new OrderInfoDTO();
                BeanUtils.copyProperties(item, orderInfo);

                try {

                    // 订单分润
                    orderManager.splitProfit(orderInfo);

                } catch (OptimusException e) {
                    log.warn("订单分润异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
                    continue;
                } catch (Exception e) {
                    log.error("订单分润异常:", e);
                    continue;
                }

            }

        }

    }

    /**
     * 初始化
     */
    private void init() {

        // 间隔
        String value = super.loadSystemConfig(CommonSystemConfigBaseKeyEnum.SPLIT_PROFIT_INTERVAL.getCode());
        if (StringUtils.hasLength(value)) {
            splitProfitInterval = Integer.parseInt(value);
        }

    }

    /**
     * 获取订单信息Query
     * 
     * @return
     */
    private OrderInfoQuery getOrderInfoQuery() {

        // 分片
        Map<Integer, Integer> shardingMap = super.sharding(CommonSystemConfigBaseKeyEnum.SPLIT_PROFIT_JOB_SHARDING.getCode());
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
        query.setSplitProfitStatus(OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_N.getCode());
        query.setPayTime(DateUtil.offsetForMinute(DateUtil.currentDate(), -splitProfitInterval));

        return query;

    }

}
