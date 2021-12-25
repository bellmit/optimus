package com.optimus.dao.mapper;

import java.util.Date;
import java.util.List;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.query.OrderInfoQuery;

import org.apache.ibatis.annotations.Param;

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
     * 根据会员编号和上游订单编号查询订单信息
     * 
     * @param memberId
     * @param callerOrderId
     * @return
     */
    OrderInfoDO getOrderInfoByMemberIdAndCallerOrderId(@Param("memberId") String memberId, @Param("callerOrderId") String callerOrderId);

    /**
     * 根据订单信息Query查询订单信息
     *
     * @param query
     * @return
     */
    List<OrderInfoDO> listOrderInfoForJobByOrderInfoQuerys(OrderInfoQuery query);

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
     * 根据主键和订单状态更新订单信息
     * 
     * @param orderInfoDO
     * @param originOrderStatus
     * @return
     */
    int updateOrderInfoByIdAndOrderStatus(@Param("orderInfo") OrderInfoDO orderInfoDO, @Param("originOrderStatus") String originOrderStatus);

    /**
     * 根据订单编号和订单状态更新订单信息
     *
     * @param orderInfoDO
     * @param originOrderStatus
     * @return
     */
    int updateOrderInfoByOrderIdAndOrderStatus(@Param("orderInfo") OrderInfoDO orderInfoDO, @Param("originOrderStatus") String originOrderStatus);

    /**
     * 根据订单编号和释放状态更新订单信息
     * 
     * @param orderId
     * @param releaseStatus
     * @param originReleaseStatus
     * @param updateTime
     * @return
     */
    int updateOrderInfoByOrderIdAndReleaseStatus(@Param("orderId") String orderId, @Param("releaseStatus") String releaseStatus, @Param("originReleaseStatus") String originReleaseStatus, @Param("updateTime") Date updateTime);

    /**
     * 根据订单编号和分润状态更新订单信息
     * 
     * @param orderId
     * @param splitProfitStatus
     * @param originSplitProfitStatus
     * @param updateTime
     * @return
     */
    int updateOrderInfoByOrderIdAndSplitProfitStatus(@Param("orderId") String orderId, @Param("splitProfitStatus") String splitProfitStatus, @Param("originSplitProfitStatus") String originSplitProfitStatus, @Param("updateTime") Date updateTime);

    /**
     * 更新订单信息:回调商户
     * 
     * @param orderInfoDO
     * @return
     */
    int updateOrderInfoForMerchantCallback(OrderInfoDO orderInfoDO);

    /**
     * 更新订单信息:渠道订单查询
     * 
     * @param orderInfoDO
     * @return
     */
    int updateOrderInfoForChannelOrderQuery(OrderInfoDO orderInfoDO);

}