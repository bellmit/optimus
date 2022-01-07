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
import com.optimus.util.constants.common.CommonSystemConfigBaseKeyEnum;
import com.optimus.util.constants.order.OrderReleaseStatusEnum;
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
 * 释放订单
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class ReleaseOrderJob extends BaseOrderJob {

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

                    // 释放订单
                    orderManager.release(orderInfo);

                } catch (OptimusException e) {
                    log.error("释放订单异常:", e);
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

        // 间隔
        String value = super.loadSystemConfig(CommonSystemConfigBaseKeyEnum.RELEASE_ORDER_INTERVAL.getCode());
        if (StringUtils.hasLength(value)) {
            releaseOrderInterval = Integer.parseInt(value);
        }

    }

    /**
     * 获取订单信息Query
     * 
     * @return
     */
    private OrderInfoQuery getOrderInfoQuery() {

        // 分片
        Map<Integer, Integer> shardingMap = super.sharding(CommonSystemConfigBaseKeyEnum.RELEASE_ORDER_JOB_SHARDING.getCode());
        if (CollectionUtils.isEmpty(shardingMap)) {
            return null;
        }

        // 订单信息Query
        OrderInfoQuery query = new OrderInfoQuery();
        query.setPage(new Page(1, BaseOrderJob.BASE_ORDER_JOB_PAGE_SIZE));
        query.setShard(shardingMap.entrySet().stream().findFirst().get().getKey());
        query.setTotalShard(shardingMap.entrySet().stream().findFirst().get().getValue());
        query.setOrderType(OrderTypeEnum.ORDER_TYPE_C.getCode());
        query.setReleaseStatus(OrderReleaseStatusEnum.RELEASE_STATUS_N.getCode());
        query.setCreateTime(DateUtil.offsetForMinute(DateUtil.currentDate(), -releaseOrderInterval));

        return query;

    }

}
