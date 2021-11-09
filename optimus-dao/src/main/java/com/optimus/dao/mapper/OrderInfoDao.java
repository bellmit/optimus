package com.optimus.dao.mapper;

import com.optimus.dao.domain.OrderInfoDO;

/**
 * OrderInfoDao
 * 
 * @author sunxp
 */
public interface OrderInfoDao {

    /**
     * getOrderInfoById
     * 
     * @param id
     * @return
     */
    OrderInfoDO getOrderInfoById(Long id);

    /**
     * addOrderInfo
     * 
     * @param orderInfoDO
     * @return
     */
    int addOrderInfo(OrderInfoDO orderInfoDO);

    /**
     * updateOrderInfo
     * 
     * @param orderInfoDO
     * @return
     */
    int updateOrderInfo(OrderInfoDO orderInfoDO);

}