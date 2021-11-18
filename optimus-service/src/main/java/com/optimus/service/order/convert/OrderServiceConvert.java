package com.optimus.service.order.convert;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.query.OrderInfoQuery;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.service.order.dto.CreateOrderDTO;
import com.optimus.service.order.dto.OrderInfoDTO;
import com.optimus.service.order.dto.PayOrderDTO;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
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
     * 获取账户交易DTO
     * 
     * @param accountChangeTypeEnum
     * @param createOrder
     * @param remark
     * @return DoTransDTO
     */
    public static DoTransDTO getDoTransDTO(AccountChangeTypeEnum accountChangeTypeEnum, CreateOrderDTO createOrder, String remark) {

        DoTransDTO doTrans = new DoTransDTO();

        doTrans.setChangeType(accountChangeTypeEnum.getCode());
        doTrans.setMemberId(createOrder.getMemberId());
        doTrans.setOrderId(createOrder.getOrderId());
        doTrans.setOrderType(createOrder.getOrderType());
        doTrans.setAmount(createOrder.getActualAmount());

        return doTrans;

    }

    /**
     * 获取账户交易DTO
     *
     * @param accountChangeTypeEnum
     * @param payOrder
     * @param remark
     * @return DoTransDTO
     */
    public static DoTransDTO getDoTransDTO(AccountChangeTypeEnum accountChangeTypeEnum, PayOrderDTO payOrder, String remark) {

        DoTransDTO doTrans = new DoTransDTO();

        doTrans.setChangeType(accountChangeTypeEnum.getCode());
        doTrans.setMemberId(payOrder.getMemberId());
        doTrans.setOrderId(payOrder.getOrderId());
        doTrans.setOrderType(payOrder.getOrderType());
        doTrans.setAmount(payOrder.getActualAmount());

        return doTrans;

    }

}
