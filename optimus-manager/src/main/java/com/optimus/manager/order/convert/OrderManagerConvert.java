package com.optimus.manager.order.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.optimus.util.constants.order.OrderReleaseStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.util.model.page.Page;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * 订单ManagerConvert
 *
 * @author sunxp
 */
public class OrderManagerConvert {

    /**
     * 获取订单信息DO
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
     * 获取账户交易
     * 
     * @param orderInfo
     * @param chainList
     * @param map
     * @return
     */
    public static List<DoTransDTO> getDoTransDTOList(OrderInfoDTO orderInfo, List<MemberInfoChainResult> chainList, Map<String, BigDecimal> map) {

        // 账户交易List
        List<DoTransDTO> doTransList = new ArrayList<>();

        // 排序:码商[n]->代理[n]->管理[1]->平台[1]
        chainList.sort((l, r) -> Short.compare(l.getLevel(), r.getLevel()));
        chainList.stream().reduce((l, r) -> {

            String changeType = null;
            BigDecimal amount = null;

            // 码商->码商
            if (StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), r.getMemberType())) {
                // 第一个码商
                if (StringUtils.pathEquals(orderInfo.getCodeMemberId(), l.getMemberId())) {

                    changeType = AccountChangeTypeEnum.B_PLUS.getCode();
                    amount = orderInfo.getActualAmount().multiply(map.get(l.getMemberId()));

                    if (!StringUtils.pathEquals(OrderReleaseStatusEnum.RELEASE_STATUS_D.getCode(), orderInfo.getReleaseStatus())) {
                        changeType = AccountChangeTypeEnum.B_MINUS.getCode();
                        amount = orderInfo.getActualAmount().subtract(amount);
                    }

                    doTransList.add(new DoTransDTO(l.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "渠道回调码商分润"));
                }

                // 其他码商
                changeType = AccountChangeTypeEnum.B_PLUS.getCode();
                amount = orderInfo.getActualAmount().multiply(map.get(r.getMemberId()).subtract(map.get(l.getMemberId())));
                doTransList.add(new DoTransDTO(r.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "渠道回调码商分润"));

                return r;
            }

            // 代理->管理->平台
            if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), l.getMemberType())) {
                // 平台
                if (StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_S.getCode(), r.getMemberType())) {

                    changeType = AccountChangeTypeEnum.P_PLUS.getCode();
                    amount = orderInfo.getActualAmount().multiply(map.get(l.getMemberId()));
                    doTransList.add(new DoTransDTO(r.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "渠道回调平台分润"));

                    return r;
                }

                // 管理/代理
                changeType = AccountChangeTypeEnum.A_PLUS.getCode();
                amount = orderInfo.getActualAmount().multiply(map.get(l.getMemberId()).subtract(map.get(r.getMemberId())));
                doTransList.add(new DoTransDTO(r.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "渠道回调管理或代理分润"));

                return r;
            }

            // 代理-商户-码商
            if (StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), l.getMemberType()) && StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), r.getMemberType())) {
                // 代理
                changeType = AccountChangeTypeEnum.A_MINUS.getCode();
                amount = orderInfo.getActualAmount().multiply(map.get(r.getMemberId()));
                doTransList.add(new DoTransDTO(r.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "渠道回调代理系统使用费"));

                changeType = AccountChangeTypeEnum.E_PLUS.getCode();
                amount = orderInfo.getActualAmount().multiply(map.get(orderInfo.getMemberId()).subtract(map.get(l.getMemberId())));
                doTransList.add(new DoTransDTO(r.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "渠道回调代理收益"));

                // 商户
                changeType = AccountChangeTypeEnum.B_PLUS.getCode();
                amount = orderInfo.getActualAmount().subtract(orderInfo.getActualAmount().multiply(map.get(orderInfo.getMemberId())));
                doTransList.add(new DoTransDTO(orderInfo.getMemberId(), orderInfo.getOrderId(), orderInfo.getOrderType(), changeType, amount, "渠道回调商户费用"));

                return r;
            }

            throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "渠道回调账户交易构建记账异常");
        });

        return doTransList;
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
     * @param createOrder
     * @param output
     * @return
     */
    public static OrderInfoDTO getOrderInfoDTO(CreateOrderDTO createOrder, ExecuteScriptOutputDTO output) {

        // 订单信息DTO
        OrderInfoDTO orderInfo = new OrderInfoDTO();
        BeanUtils.copyProperties(createOrder, orderInfo);

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

}
