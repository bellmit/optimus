package com.optimus.service.order.convert;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.query.OrderInfoQuery;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.util.DateUtil;
import com.optimus.util.page.Page;
import org.springframework.beans.BeanUtils;

/**
 * 订单Service转换器
 *
 * @author sunxp
 */
public class OrderServiceConvert {

    /**
     * 获取订单查询对象
     *
     * @param orderInfo
     * @param page
     * @return
     */
    public static OrderInfoQuery getOrderInfoQuery(OrderInfoDTO orderInfo, Page page) {

        OrderInfoQuery query = new OrderInfoQuery();

        query.setPage(page);
        query.setMemberId(orderInfo.getMemberId());
        query.setOrderId(orderInfo.getOrderId());
        query.setCallerOrderId(orderInfo.getCallerOrderId());

        return query;

    }

    /**
     * OrderInfoDTO转DO
     *
     * @param orderInfo
     * @return
     */
    public static OrderInfoDO getOrderInfoDO(OrderInfoDTO orderInfo) {

        OrderInfoDO orderInfoDO = new OrderInfoDO();

        BeanUtils.copyProperties(orderInfo, orderInfoDO);
        orderInfoDO.setCreateTime(DateUtil.currentDate());
        orderInfoDO.setUpdateTime(DateUtil.currentDate());
        orderInfoDO.setOrderTime(DateUtil.currentDate());

        return orderInfoDO;
    }

    /**
     * createOrderDTODTO转OrderInfoDTO
     *
     * @param createOrder
     * @return
     */
    public static OrderInfoDTO getOrderInfoDTO(CreateOrderDTO createOrder) {
        OrderInfoDTO orderInfo = new OrderInfoDTO();
        BeanUtils.copyProperties(createOrder, orderInfo);
        return orderInfo;
    }

    /**
     * 获取订单查询对象
     *
     * @param orderInfo
     * @param page
     * @return
     */
    /**
     * 获取账户交易DTO
     *
     * @param memberId  会员编号
     * @param orderId   订单编号
     * @param orderType 订单类型
     * @return DoTransDTO
     */
    public static DoTransDTO getDoTransDTO(String memberId, String orderId, String orderType) {
        DoTransDTO doTrans = new DoTransDTO();
        doTrans.setMemberId(memberId);
        doTrans.setOrderId(orderId);
        doTrans.setOrderType(orderType);
        return doTrans;

    }

}
