package com.optimus.manager.order.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.optimus.dao.domain.GatewaySubChannelDO;
import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.OrderNoticeDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;
import com.optimus.util.constants.gateway.ScriptEnum;
import com.optimus.util.constants.member.MemberTypeEnum;
import com.optimus.util.constants.order.OrderReleaseStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * 订单ManagerConvert
 *
 * @author sunxp
 */
public class OrderManagerConvert {

    /**
     * 获取账户交易List
     * 
     * @param orderInfo
     * @param chainList
     * @param memberChannelList
     * @return
     */
    public static List<DoTransDTO> getDoTransDTOList(OrderInfoDTO orderInfo, List<MemberInfoChainResult> chainList, List<MemberChannelDO> memberChannelList) {

        // 账户交易List
        List<DoTransDTO> doTransList = new ArrayList<>();

        // 构建会员费率Map
        Map<String, BigDecimal> map = memberChannelList.stream().collect(Collectors.toMap(MemberChannelDO::getMemberId, MemberChannelDO::getRate));

        // 排序:码商[n]->代理[n]->管理[1]->平台[1]
        chainList.sort((l, r) -> Short.compare(l.getLevel(), r.getLevel()));
        chainList.stream().reduce((l, r) -> {

            String changeType = null;
            BigDecimal amount = null;

            // 第一个码商
            if (StringUtils.pathEquals(orderInfo.getCodeMemberId(), l.getMemberId())) {
                changeType = AccountChangeTypeEnum.B_PLUS.getCode();
                amount = orderInfo.getActualAmount().multiply(map.get(l.getMemberId()));

                // 释放
                if (!StringUtils.pathEquals(OrderReleaseStatusEnum.RELEASE_STATUS_D.getCode(), orderInfo.getReleaseStatus())) {
                    changeType = AccountChangeTypeEnum.B_MINUS.getCode();
                    amount = orderInfo.getActualAmount().subtract(amount);
                }

                doTransList.add(new DoTransDTO(l.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "码商分润"));
            }

            // 码商->码商
            if (StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), r.getMemberType())) {
                changeType = AccountChangeTypeEnum.B_PLUS.getCode();
                amount = orderInfo.getActualAmount().multiply(map.get(r.getMemberId()).subtract(map.get(l.getMemberId())));
                doTransList.add(new DoTransDTO(r.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "码商分润"));
                return r;
            }

            // 代理->管理->平台
            if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), l.getMemberType())) {
                // 平台
                if (StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_S.getCode(), r.getMemberType())) {
                    changeType = AccountChangeTypeEnum.P_PLUS.getCode();
                    amount = orderInfo.getActualAmount().multiply(map.get(l.getMemberId()));
                    doTransList.add(new DoTransDTO(r.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "平台分润"));
                    return r;
                }

                // 管理/代理
                changeType = AccountChangeTypeEnum.A_PLUS.getCode();
                amount = orderInfo.getActualAmount().multiply(map.get(l.getMemberId()).subtract(map.get(r.getMemberId())));
                doTransList.add(new DoTransDTO(r.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, MemberTypeEnum.instanceOf(r.getMemberType()).getMemo() + "分润"));

                return r;
            }

            // 代理-商户-码商
            if (StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), l.getMemberType()) && StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), r.getMemberType())) {
                // 代理
                changeType = AccountChangeTypeEnum.A_MINUS.getCode();
                amount = orderInfo.getActualAmount().multiply(map.get(r.getMemberId()));
                doTransList.add(new DoTransDTO(r.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "代理系统使用费"));

                changeType = AccountChangeTypeEnum.E_PLUS.getCode();
                amount = orderInfo.getActualAmount().multiply(map.get(orderInfo.getMemberId()).subtract(map.get(l.getMemberId())));
                doTransList.add(new DoTransDTO(r.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "代理收益"));

                // 商户
                changeType = AccountChangeTypeEnum.B_PLUS.getCode();
                amount = orderInfo.getActualAmount().subtract(orderInfo.getActualAmount().multiply(map.get(orderInfo.getMemberId())));
                doTransList.add(new DoTransDTO(orderInfo.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "商户费用"));

                return r;
            }

            throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "获取账户交易对象异常");
        });

        return doTransList;
    }

    /**
     * 获取订单信息DO
     * 
     * @param payOrder
     * @param splitProfitStatus
     * @return
     */
    public static OrderInfoDO getOrderInfoDO(PayOrderDTO payOrder, String splitProfitStatus) {

        // 订单信息DO
        OrderInfoDO orderInfo = new OrderInfoDO();

        orderInfo.setId(payOrder.getId());
        orderInfo.setOrderStatus(payOrder.getOrderStatus());
        orderInfo.setSplitProfitStatus(splitProfitStatus);
        orderInfo.setPayTime(DateUtil.currentDate());
        orderInfo.setBehavior(payOrder.getBehavior());
        orderInfo.setUpdateTime(DateUtil.currentDate());

        return orderInfo;
    }

    /**
     * 获取订单信息DO
     *
     * @param orderInfo
     * @return
     */
    public static OrderInfoDO getOrderInfoDO(OrderInfoDTO orderInfo) {

        // 订单信息DO
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        BeanUtils.copyProperties(orderInfo, orderInfoDO);

        orderInfoDO.setCreateTime(DateUtil.currentDate());
        orderInfoDO.setUpdateTime(DateUtil.currentDate());
        orderInfoDO.setOrderTime(DateUtil.currentDate());

        return orderInfoDO;
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
        doTrans.setRemark(remark);

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
        doTrans.setRemark(remark);

        return doTrans;

    }

    /**
     * 获取码商账户交易DTO
     *
     * @param accountChangeTypeEnum
     * @param orderInfo
     * @param remark
     * @return DoTransDTO
     */
    public static DoTransDTO getDoTransForCode(AccountChangeTypeEnum accountChangeTypeEnum, OrderInfoDTO orderInfo, String remark) {

        // 账户交易DTO
        DoTransDTO doTrans = new DoTransDTO();

        doTrans.setChangeType(accountChangeTypeEnum.getCode());
        doTrans.setMemberId(orderInfo.getCodeMemberId());
        doTrans.setOrderId(orderInfo.getOrderId());
        doTrans.setOrderType(orderInfo.getOrderType());
        doTrans.setAmount(orderInfo.getActualAmount());
        doTrans.setRemark(remark);

        return doTrans;

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

        // 等待支付
        orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_NP.getCode());

        return orderInfo;

    }

    /**
     * 获取订单信息DTO
     * 
     * @param payOrder
     * @return
     */
    public static OrderInfoDTO getOrderInfoDTO(PayOrderDTO payOrder) {

        // 订单信息DTO
        OrderInfoDTO orderInfo = new OrderInfoDTO();
        BeanUtils.copyProperties(payOrder, orderInfo);

        return orderInfo;

    }

    /**
     * 获取订单信息DTO
     * 
     * @param orderInfoDO
     * @return
     */
    public static OrderInfoDTO getOrderInfoDTO(OrderInfoDO orderInfoDO) {

        // 订单信息DTO
        OrderInfoDTO orderInfo = new OrderInfoDTO();
        BeanUtils.copyProperties(orderInfoDO, orderInfo);

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

        orderInfo.setOutput(output);
        orderInfo.setChannelCode(createOrder.getGatewayChannel().getChannelCode());
        orderInfo.setSubChannelCode(createOrder.getGatewaySubChannel().getChannelCode());
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
        if (StringUtils.pathEquals(GatewayChannelGroupEnum.CHANNEL_GROUP_I.getCode(), createOrder.getGatewayChannel().getChannelGroup())) {
            orderInfo.setCodeMemberId(output.getCodeMemberId());
        }

        // 验证码商会员编号
        if (!StringUtils.hasLength(orderInfo.getCodeMemberId())) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;
        }

        orderInfo.setMerchantCallbackCount((short) 0);
        orderInfo.setChannelOrderQueryCount((short) 0);

        return orderInfo;

    }

    /**
     * 获取执行脚本输入DTO
     * 
     * 下单
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
            input.setImplPath(createOrder.getGatewaySubChannel().getImplPath());
            input.setBizContent(createOrder.getGatewaySubChannel().getBizContent());
        }

        return input;

    }

    /**
     * 获取执行脚本输入DTO
     * 
     * 渠道订单查询
     * 
     * @param orderInfo
     * @param gatewaySubChannel
     * @return
     */
    public static ExecuteScriptInputDTO getExecuteScriptInputDTO(OrderInfoDO orderInfo, GatewaySubChannelDO gatewaySubChannel) {

        // 执行脚本输入DTO
        ExecuteScriptInputDTO input = new ExecuteScriptInputDTO();
        input.setScriptMethod(ScriptEnum.QUERY.getCode());
        input.setMemberId(orderInfo.getMemberId());
        input.setOrderId(orderInfo.getOrderId());
        input.setCalleeOrderId(orderInfo.getCalleeOrderId());
        input.setImplPath(gatewaySubChannel.getImplPath());
        input.setBizContent(gatewaySubChannel.getBizContent());

        return input;

    }

    /**
     * 获取订单通知DTO
     * 
     * @param orderInfo
     * @return
     */
    public static OrderNoticeDTO getOrderNoticeDTO(OrderInfoDTO orderInfo) {

        // 订单通知DTO
        OrderNoticeDTO input = new OrderNoticeDTO();

        input.setMemberId(orderInfo.getMemberId());
        input.setOrderId(orderInfo.getOrderId());
        input.setCallerOrderId(orderInfo.getCallerOrderId());
        input.setOrderStatus(orderInfo.getOrderStatus());
        input.setOrderAmount(orderInfo.getOrderAmount());
        input.setActualAmount(orderInfo.getActualAmount());

        return input;

    }

}
