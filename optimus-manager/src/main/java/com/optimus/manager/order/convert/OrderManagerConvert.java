package com.optimus.manager.order.convert;

import java.util.ArrayList;
import java.util.List;

import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.query.OrderInfoQuery;
import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.OrderNoticeInputDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;
import com.optimus.util.constants.gateway.ScriptEnum;
import com.optimus.util.constants.member.MemberTypeEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.util.page.Page;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * 订单ManagerConvert
 *
 * @author sunxp
 */
public class OrderManagerConvert {

    private static final String MEMBER_TYPE_A = MemberTypeEnum.MEMBER_TYPE_A.getCode();
    private static final String MEMBER_TYPE_C = MemberTypeEnum.MEMBER_TYPE_C.getCode();

    /**
     * 获取账户交易DTO
     * 
     * @param accountChangeTypeEnum
     * @param createOrder
     * @param remark
     * @return DoTransDTO
     */
    public static DoTransDTO getDoTransDTO(AccountChangeTypeEnum accountChangeTypeEnum, CreateOrderDTO createOrder, String remark) {

        // 账户交易DTO
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

        // 账户交易DTO
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

        // 账户交易DTO
        DoTransDTO doTrans = new DoTransDTO();

        doTrans.setChangeType(accountChangeTypeEnum.getCode());
        doTrans.setMemberId(orderInfo.getMemberId());
        doTrans.setOrderId(orderInfo.getOrderId());
        doTrans.setOrderType(orderInfo.getOrderType());
        doTrans.setAmount(orderInfo.getActualAmount());

        return doTrans;

    }

    /**
     * 获取账户交易DTO
     * 
     * @param chainList
     * @param memberChannelList
     * @param payOrder
     * @param originOrderStatus
     * @return
     */
    public static List<DoTransDTO> getDoTransDTOList(List<MemberInfoChainResult> chainList, List<MemberChannelDO> memberChannelList, PayOrderDTO payOrder, String originOrderStatus) {

        // 账户交易List
        List<DoTransDTO> doTransList = new ArrayList<>();

        // 排序:码商[n]->代理[n]->管理[1]->平台[1]
        chainList.sort((l, r) -> Short.compare(l.getLevel(), r.getLevel()));

        chainList.stream().reduce((l, r) -> {

            // 类型
            String lMemberType = l.getMemberType();
            String rMemberType = r.getMemberType();

            // 平台

            // 撮合到的码商

            // 码商->码商:r-l
            if (StringUtils.pathEquals(MEMBER_TYPE_C, rMemberType)) {

            }

            // 代理->管理->平台:l-r
            if (!StringUtils.pathEquals(MEMBER_TYPE_C, lMemberType)) {

            }

            // 代理-商户-码商:商户-码商
            if (StringUtils.pathEquals(MEMBER_TYPE_C, lMemberType) && StringUtils.pathEquals(MEMBER_TYPE_A, rMemberType)) {

            }

            throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "获取账户交易异常");

        });

        return doTransList;

    }

    /**
     * 获取订单Query
     *
     * @param orderInfo
     * @param page
     * @return
     */
    public static OrderInfoQuery getOrderInfoQuery(OrderInfoDTO orderInfo, Page page) {

        // 订单Query
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

        // 订单信息DTO
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

        // 订单信息DTO
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

        // 订单信息DTO
        OrderInfoDTO orderInfo = new OrderInfoDTO();
        BeanUtils.copyProperties(createOrder, orderInfo);

        orderInfo.setCalleeOrderId(output.getCalleeOrderId());
        orderInfo.setOrderStatus(output.getOrderStatus());
        orderInfo.setActualAmount(output.getActualAmount());
        orderInfo.setChannelReturnMessage(output.getChannelReturnMessage());

        // 下单成功必须为等待支付
        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus())) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;
        }

        // 自研渠道设置码商会员编号
        if (StringUtils.pathEquals(GatewayChannelGroupEnum.GATEWAY_CHANNEL_GROUP_I.getCode(), createOrder.getGatewayChannel().getChannelGroup())) {
            orderInfo.setCodeMemberId(output.getCodeMemberId());
        }

        // 验证码商会员编号
        if (!StringUtils.hasLength(orderInfo.getCodeMemberId())) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;
        }

        return orderInfo;

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

        // 执行脚本输入DTO
        ExecuteScriptInputDTO input = new ExecuteScriptInputDTO();

        input.setScriptMethod(ScriptEnum.CREATE.getCode());
        input.setMemberId(createOrder.getMemberId());
        input.setOrderId(createOrder.getOrderId());
        input.setAmount(createOrder.getOrderAmount());
        input.setOrderTime(createOrder.getOrderTime());

        // 下单必要参数
        if (StringUtils.pathEquals(OrderTypeEnum.ORDER_TYPE_C.getCode(), createOrder.getOrderType())) {
            input.setClientIp(createOrder.getClientIp());
            input.setRedirectUrl(createOrder.getRedirectUrl());
            input.setImplType(createOrder.getGatewaySubChannel().getImplType());
            input.setImplPath(createOrder.getGatewaySubChannel().getImplPath());
            input.setBizContent(createOrder.getGatewaySubChannel().getBizContent());
        }

        return input;

    }

    /**
     * 获取订单通知输入DTO
     * 
     * @param payOrder
     * @return
     */
    public static OrderNoticeInputDTO getOrderNoticeInputDTO(PayOrderDTO payOrder) {

        // 订单通知输入DTO
        OrderNoticeInputDTO input = new OrderNoticeInputDTO();

        input.setMemberId(payOrder.getMemberId());
        input.setOrderId(payOrder.getOrderId());
        input.setCallerOrderId(payOrder.getCallerOrderId());
        input.setOrderStatus(payOrder.getOrderStatus());
        input.setOrderAmount(payOrder.getOrderAmount());
        input.setActualAmount(payOrder.getActualAmount());

        return input;

    }

}
