package com.optimus.dao.mapper;

import com.optimus.dao.domain.OrderInfoDO;

/**
 * 订单信息Dao
 * 
 * @author sunxp
 */
public interface OrderInfoDao {

    /**
     * 根据主键查询订单信息
     * 
     * @param id
     * @return
     */
    OrderInfoDO getOrderInfoById(Long id);

    /**
     * 新增一条订单信息
     * 
     * @param orderInfoDO
     * @return
     */
    int addOrderInfo(OrderInfoDO orderInfoDO);

    /**
     * 更新订单信息
     * 
     * @param orderInfoDO
     * @return
     */
    int updateOrderInfo(OrderInfoDO orderInfoDO);

}