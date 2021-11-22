package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RespCodeEnum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum RespCodeEnum {

    /* ---------系统类编码--------- */

    SUCCESS("1000", "成功"),

    FAILE("1001", "失败"),

    INVALID_IP("1002", "IP异常"),

    INVALID_PARAM("1003", "无效参数"),

    ERROR_CONVERT("1004", "转换异常"),

    ERROR_SIGN("1005", "签名异常"),

    /* ---------会员类编码--------- */

    MEMBER_NO("2000", "会员不存在"),

    MEMBER_ERROR("2001", "会员信息异常"),

    MEMBER_TYPE_ERROR("2002", "会员类型不匹配"),

    MEMBER_TRANS_PERMISSION_ERROR("2003", "会员交易限制异常"),

    MEMBER_CHANNEL_ERROR("2004", "会员渠道异常"),

    /* ---------账户类编码--------- */

    ACCOUNT_NO("3000", "账户不存在"),

    ACCOUNT_AMOUNT_ERROR("3001", "账户余额不足"),

    ACCOUNT_TRANSACTION_ERROR("3002", "账户交易异常"),

    /* ---------订单类编码--------- */

    ORDER_NO("4000", "订单不存在"),

    ORDER_EXIST_ERROR("4001", "订单已存在"),

    ORDER_PLACE_ERROR("4002", "下单失败"),

    ORDER_ERROR("4003", "订单信息异常"),

    /* ---------网关类编码--------- */

    GATEWAY_CHANNEL_NO("5000", "网关渠道不存在"),

    GATEWAY_CHANNEL_ERROR("5001", "网关渠道信息异常"),

    GATEWAY_EXECUTE_SCRIPT_ERROR("5002", "网关渠道脚本执行异常"),

    ;

    private String code;
    private String memo;

}
