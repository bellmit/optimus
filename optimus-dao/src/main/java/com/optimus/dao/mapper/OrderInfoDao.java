package com.optimus.dao.mapper;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.query.OrderInfoQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * 根据订单编号查询订单信息
     *
     * @param orderId
     * @return OrderInfoDO
     */
    OrderInfoDO getOrderInfoByOrderId(String orderId);

    /**
     * 根据上游订单编号查询订单信息
     *
     * @param callerOrderId
     * @return OrderInfoDO
     */
    OrderInfoDO getOrderInfoByCallerOrderId(String callerOrderId);

    /**
     * 根据OrderInfoQuery查询订单信息
     *
     * @param query
     * @return
     */
    List<OrderInfoDO> listOrderInfoByOrderInfoQuerys(OrderInfoQuery query);

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

    /**
     * 根据订单号更新订单状态
     *
     * @param orderId
     * @param orderStatus
     * @return
     */
    int updateStatusByOrderId(@Param("orderId") String orderId, @Param("orderStatus") String orderStatus);

}