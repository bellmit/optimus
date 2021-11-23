package com.optimus.manager.order.convert;

import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.query.OrderInfoQuery;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;
import com.optimus.util.constants.gateway.ScriptEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.page.Page;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * 订单ManagerConvert
 *
 * @author sunxp
 */
public class OrderManagerConvert {

    /**
     * 获取订单Query
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
     * 获取订单信息DTO
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
     * 获取订单信息DTO
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
     * 获取订单信息DTO
     *
     * @param createOrder
     * @param output
     * @return
     */
    public static OrderInfoDTO getOrderInfoDTO(CreateOrderDTO createOrder, ExecuteScriptOutputDTO output) {

        OrderInfoDTO orderInfo = new OrderInfoDTO();
        BeanUtils.copyProperties(createOrder, orderInfo);

        orderInfo.setCalleeOrderId(output.getCalleeOrderId());
        orderInfo.setOrderStatus(output.getOrderStatus());
        orderInfo.setActualAmount(output.getActualAmount());
        orderInfo.setChannelReturnMessage(output.getChannelReturnMessage());

        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus())) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;
        }

        if (StringUtils.pathEquals(GatewayChannelGroupEnum.GATEWAY_CHANNEL_GROUP_I.getCode(), createOrder.getGatewayChannel().getChannelGroup())) {
            orderInfo.setCodeMemberId(output.getCodeMemberId());
        }

        if (!StringUtils.hasLength(orderInfo.getCodeMemberId())) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;
        }

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

    /**
     * 获取账户交易DTO
     *
     * @param accountChangeTypeEnum
     * @param orderInfo
     * @param remark
     * @return DoTransDTO
     */
    public static DoTransDTO getDoTransDTO(AccountChangeTypeEnum accountChangeTypeEnum, OrderInfoDTO orderInfo, String remark) {

        DoTransDTO doTrans = new DoTransDTO();

        doTrans.setChangeType(accountChangeTypeEnum.getCode());
        doTrans.setMemberId(orderInfo.getMemberId());
        doTrans.setOrderId(orderInfo.getOrderId());
        doTrans.setOrderType(orderInfo.getOrderType());
        doTrans.setAmount(orderInfo.getActualAmount());

        return doTrans;

    }

    /**
     * 获取执行脚本输入DTO
     * 
     * 创建订单
     * 
     * @param createOrder
     * @return
     */
    public static ExecuteScriptInputDTO getExecuteScriptInputDTO(CreateOrderDTO createOrder) {

        ExecuteScriptInputDTO input = new ExecuteScriptInputDTO();
        input.setScriptMethod(ScriptEnum.CREATE.getCode());
        input.setMemberId(createOrder.getMemberId());
        input.setOrderId(createOrder.getOrderId());
        input.setAmount(createOrder.getOrderAmount());
        input.setOrderTime(createOrder.getOrderTime());

        if (StringUtils.pathEquals(OrderTypeEnum.ORDER_TYPE_C.getCode(), createOrder.getOrderType())) {
            input.setClientIp(createOrder.getClientIp());
            input.setRedirectUrl(createOrder.getRedirectUrl());
            input.setImplType(createOrder.getGatewaySubChannel().getImplType());
            input.setImplPath(createOrder.getGatewaySubChannel().getImplPath());
            input.setBizContent(createOrder.getGatewaySubChannel().getBizContent());
        }

        return input;

    }

}
