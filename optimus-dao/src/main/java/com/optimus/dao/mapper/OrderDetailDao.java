package com.optimus.dao.mapper;

import com.optimus.dao.domain.OrderDetailDO;

/**
 * OrderDetailDao
 */
public interface OrderDetailDao {

    /**
     * getOrderDetailById
     * 
     * @param id
     * @return
     */
    OrderDetailDO getOrderDetailById(Long id);

    /**
     * addOrderDetail
     * 
     * @param orderDetailDO
     * @return
     */
    int addOrderDetail(OrderDetailDO orderDetailDO);

    /**
     * updateOrderDetail
     * 
     * @param orderDetailDO
     * @return
     */
    int updateOrderDetail(OrderDetailDO orderDetailDO);

}