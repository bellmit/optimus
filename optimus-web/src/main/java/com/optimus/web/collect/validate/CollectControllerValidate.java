package com.optimus.web.collect.validate;

import java.math.BigDecimal;
import java.util.Objects;

import com.optimus.util.AssertUtil;
import com.optimus.util.constants.BaseEnum;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.web.collect.req.ApplyForRechargeReq;
import com.optimus.web.collect.req.ApplyForWithdrawReq;
import com.optimus.web.collect.req.BaseCollectReq;
import com.optimus.web.collect.req.ConfirmForRechargeReq;
import com.optimus.web.collect.req.ConfirmForWithdrawReq;
import com.optimus.web.collect.req.PlaceOrderReq;
import com.optimus.web.collect.req.RechargeReq;
import com.optimus.web.collect.req.TransferReq;
import com.optimus.web.collect.req.WithdrawReq;

/**
 * 收单ControllerValidate
 *
 * @author sunxp
 */
public class CollectControllerValidate {

    /**
     * 验证基础参数
     *
     * @param req
     */
    public static void validateBaseCollect(BaseCollectReq req) {
        // 断言
        AssertUtil.notEmpty(req, RespCodeEnum.INVALID_PARAM, "入参对象不能为空");
        AssertUtil.notEmpty(req.getMemberId(), RespCodeEnum.INVALID_PARAM, "会员编号不能为空");
        AssertUtil.notEmpty(req.getCallerOrderId(), RespCodeEnum.INVALID_PARAM, "调用方订单编号不能为空");

        if (Objects.nonNull(req.getAmount())) {
            // 订单金额
            if (req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new OptimusException(RespCodeEnum.INVALID_PARAM, "订单金额不合法");
            }

            // 订单金额精度
            if (req.getAmount().scale() > Integer.parseInt(BaseEnum.SCALE_TWO.getCode())) {
                throw new OptimusException(RespCodeEnum.INVALID_PARAM, "订单金额精度为2");
            }
        }
    }

    /**
     * 验证商户下单
     *
     * @param req
     */
    public static void validatePlaceOrder(PlaceOrderReq req) {
        // 基础验证
        validateBaseCollect(req);
        // 自身业务
        AssertUtil.notEmpty(req.getAmount(), RespCodeEnum.INVALID_PARAM, "订单金额不能为空");
        AssertUtil.notEmpty(req.getChannelCode(), RespCodeEnum.INVALID_PARAM, "网关渠道编号不能为空");
        AssertUtil.notEmpty(req.getMerchantCallBackUrl(), RespCodeEnum.INVALID_PARAM, "回调商户地址不能为空");
    }

    /**
     * 验证申请充值
     *
     * @param req
     */
    public static void validateApplyForRecharge(ApplyForRechargeReq req) {
        // 基础验证
        validateBaseCollect(req);
        // 自身业务
        AssertUtil.notEmpty(req.getAmount(), RespCodeEnum.INVALID_PARAM, "订单金额不能为空");
    }

    /**
     * 验证确认充值
     *
     * @param req
     */
    public static void validateConfirmForRecharge(ConfirmForRechargeReq req) {
        // 基础验证
        validateBaseCollect(req);
        // 自身业务
        AssertUtil.notEmpty(req.getOrderId(), RespCodeEnum.INVALID_PARAM, "订单编号不能为空");
        AssertUtil.notEmpty(req.getSubMemberId(), RespCodeEnum.INVALID_PARAM, "下级会员编号不能为空");
        AssertUtil.notEmpty(req.getConfirmType(), RespCodeEnum.INVALID_PARAM, "确认类型不能为空");
    }

    /**
     * 验证充值
     *
     * @param req
     */
    public static void validateRecharge(RechargeReq req) {
        // 基础验证
        validateBaseCollect(req);
        // 自身业务
        AssertUtil.notEmpty(req.getAmount(), RespCodeEnum.INVALID_PARAM, "订单金额不能为空");
        AssertUtil.notEmpty(req.getSubDirectMemberId(), RespCodeEnum.INVALID_PARAM, "直接下级会员编号不能为空");
    }

    /**
     * 验证申请提现
     *
     * @param req
     */
    public static void validateApplyForWithdraw(ApplyForWithdrawReq req) {
        // 基础验证
        validateBaseCollect(req);
        // 自身业务
        AssertUtil.notEmpty(req.getAmount(), RespCodeEnum.INVALID_PARAM, "订单金额不能为空");
    }

    /**
     * 验证确认提现
     *
     * @param req
     */
    public static void validateConfirmForWithdraw(ConfirmForWithdrawReq req) {
        // 基础验证
        validateBaseCollect(req);
        // 自身业务
        AssertUtil.notEmpty(req.getOrderId(), RespCodeEnum.INVALID_PARAM, "订单编号不能为空");
        AssertUtil.notEmpty(req.getSubMemberId(), RespCodeEnum.INVALID_PARAM, "下级会员编号不能为空");
        AssertUtil.notEmpty(req.getConfirmType(), RespCodeEnum.INVALID_PARAM, "确认类型不能为空");
    }

    /**
     * 验证提现
     *
     * @param req
     */
    public static void validateWithdraw(WithdrawReq req) {
        // 基础验证
        validateBaseCollect(req);
        // 自身业务
        AssertUtil.notEmpty(req.getAmount(), RespCodeEnum.INVALID_PARAM, "订单金额不能为空");
        AssertUtil.notEmpty(req.getSubDirectMemberId(), RespCodeEnum.INVALID_PARAM, "直接下级会员编号不能为空");
    }

    /**
     * 验证划账
     *
     * @param req
     */
    public static void validateTransfer(TransferReq req) {
        // 基础验证
        validateBaseCollect(req);
        // 自身业务
        AssertUtil.notEmpty(req.getAmount(), RespCodeEnum.INVALID_PARAM, "订单金额不能为空");
        AssertUtil.notEmpty(req.getTransferType(), RespCodeEnum.INVALID_PARAM, "划账类型不能为空");
    }

}
