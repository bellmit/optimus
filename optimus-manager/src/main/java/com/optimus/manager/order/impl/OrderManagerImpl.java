package com.optimus.manager.order.impl;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.order.OrderManager;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 订单manager实现
 *
 * @author hongp
 */
@Component
@Slf4j
public class OrderManagerImpl implements OrderManager {

    @Resource
    private OrderInfoDao orderInfoDao;

    @Override
    public void checkCallerOrderId(String callerOrderId) {

        // 查询订单信息
        OrderInfoDO orderInfo = orderInfoDao.getOrderInfoByCallerOrderId(callerOrderId);

        // 校验订单是否存在
        AssertUtil.empty(orderInfo, RespCodeEnum.ORDER_EXIST_ERROR, null);
    }
}
