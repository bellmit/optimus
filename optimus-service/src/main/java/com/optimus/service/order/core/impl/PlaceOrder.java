package com.optimus.service.order.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.mapper.MemberChannelDao;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.AccountInfoDTO;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.member.dto.MemberTransConfineDTO;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.CreateOrderDTO;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.PayOrderDTO;
import com.optimus.manager.order.validate.OrderManagerValidate;
import com.optimus.service.order.core.BaseOrder;
import com.optimus.util.AssertUtil;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.account.AccountTypeEnum;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;
import com.optimus.util.constants.member.MemberCodeBalanceSwitchEnum;
import com.optimus.util.constants.member.MemberFreezeBalanceSwitchEnum;
import com.optimus.util.constants.order.OrderReleaseStatusEnum;
import com.optimus.util.constants.order.OrderSplitProfitStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 下单
 *
 * @author hongp
 */
@Component
public class PlaceOrder extends BaseOrder {

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private GatewayManager gatewayManager;

    @Autowired
    private OrderManager orderManager;

    @Resource
    private MemberChannelDao memberChannelDao;

    @Resource
    private OrderInfoDao orderInfoDao;

    /**
     * 创建订单
     *
     * @param createOrder
     * @return OrderInfoDTO
     */
    @Override
    public OrderInfoDTO createOrder(CreateOrderDTO createOrder) {

        // 查询码商会员交易限制
        MemberTransConfineDTO memberTransConfine = memberManager.getMemberTransConfineByMemberId(createOrder.getCodeMemberId());
        AssertUtil.notEmpty(memberTransConfine.getCodeBalanceSwitch(), RespCodeEnum.MEMBER_TRANS_PERMISSION_ERROR, "未配置码商余额限制开关");

        // 验证码商余额:非自研渠道且非关闭
        if (!StringUtils.pathEquals(GatewayChannelGroupEnum.CHANNEL_GROUP_I.getCode(), createOrder.getGatewayChannel().getChannelGroup())
                && !StringUtils.pathEquals(MemberCodeBalanceSwitchEnum.CODE_BALANCE_SWITCH_N.getCode(), memberTransConfine.getCodeBalanceSwitch())) {
            // 查询账户信息
            AccountInfoDTO accountInfo = accountManager.getAccountInfoByMemberIdAndAccountType(createOrder.getCodeMemberId(), AccountTypeEnum.ACCOUNT_TYPE_B.getCode());
            if (accountInfo.getAmount().compareTo(createOrder.getOrderAmount()) < 0) {
                throw new OptimusException(RespCodeEnum.ACCOUNT_AMOUNT_ERROR);
            }
        }

        // 执行脚本
        ExecuteScriptInputDTO executeScriptInput = OrderManagerConvert.getExecuteScriptInputDTO(createOrder);
        ExecuteScriptOutputDTO output = gatewayManager.executeScript(executeScriptInput);

        // 订单信息DTO
        OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(createOrder, output);

        // 验证下单状态
        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_NP.getCode(), orderInfo.getOrderStatus())) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;
        }

        // 不冻结码商余额
        if (StringUtils.pathEquals(MemberFreezeBalanceSwitchEnum.FREEZE_BALANCE_SWITCH_N.getCode(), memberTransConfine.getFreezeBalanceSwitch())) {
            orderInfo.setReleaseStatus(OrderReleaseStatusEnum.RELEASE_STATUS_D.getCode());
            orderInfo.setSplitProfitStatus(OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_N.getCode());
            return orderInfo;
        }

        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_MINUS, orderInfo, "下单减余额户"));
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.F_PLUS, orderInfo, "下单加冻结户"));

        boolean doTrans = accountManager.doTrans(doTransList);
        if (!doTrans) {
            orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
            return orderInfo;
        }

        orderInfo.setReleaseStatus(OrderReleaseStatusEnum.RELEASE_STATUS_N.getCode());
        orderInfo.setSplitProfitStatus(OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_N.getCode());
        return orderInfo;
    }

    /**
     * 支付订单
     *
     * @param payOrder
     */
    @Override
    public void payOrder(PayOrderDTO payOrder) {

        // 查询会员信息链
        List<MemberInfoChainResult> chainList = memberManager.listMemberInfoChains(payOrder.getCodeMemberId());
        AssertUtil.notEmpty(chainList, RespCodeEnum.MEMBER_ERROR, "会员信息链不能为空");

        // 查询会员关系链的会员渠道费率
        List<String> memberIdList = chainList.stream().map(MemberInfoChainResult::getMemberId).collect(Collectors.toList());
        memberIdList.add(payOrder.getMemberId());
        List<MemberChannelDO> memberChannelList = memberChannelDao.listMemberChannelByMemberIdLists(memberIdList);

        // 验证链及渠道
        OrderManagerValidate.validateChainAndChannel(chainList, memberChannelList, payOrder.getCodeMemberId());

        // 更新订单状态
        int update = orderInfoDao.updateOrderInfoByOrderIdAndOrderStatus(payOrder.getOrderId(), payOrder.getOrderStatus(), OrderStatusEnum.ORDER_STATUS_NP.getCode(), DateUtil.currentDate());
        if (update != 1) {
            throw new OptimusException(RespCodeEnum.ORDER_ERROR, "订单状态异常");
        }

        // 获取订单信息DTO
        OrderInfoDTO orderInfo = OrderManagerConvert.getOrderInfoDTO(payOrder);

        // 异步释放订单
        orderManager.asyncRelease(orderInfo);

        // 异步分润
        orderManager.asyncSplitProfit(orderInfo, chainList, memberChannelList);

        // 异步通知商户
        orderManager.asyncOrderNotice(orderInfo);

    }

}
