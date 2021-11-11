package com.optimus.dao.mapper;

import com.optimus.dao.domain.OrderDetailDO;

/**
 * 订单详情Dao
 * 
 * @author sunxp
 */
public interface OrderDetailDao {

    /**
     * 根据主键查询订单详情
     * 
     * @param id
     * @return
     */
    OrderDetailDO getOrderDetailById(Long id);

    /**
     * 新增一条订单详情
     * 
     * @param orderDetailDO
     * @return
     */
    int addOrderDetail(OrderDetailDO orderDetailDO);

    /**
     * 更新订单详情
     * 
     * @param orderDetailDO
     * @return
     */
    int updateOrderDetail(OrderDetailDO orderDetailDO);

}