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

    INVALID_PARAM("1002", "无效参数"),

    ERROR_CONVERT("1003", "转换异常"),

    ERROR_SIGN("1004", "签名异常"),

    /* ---------会员类编码--------- */

    MEMBER_NO("2000", "会员不存在"),

    MEMBER_ERROR("2001", "会员信息异常"),

    MEMBER_TYPE_ERROR("2002", "会员类型不匹配"),

    MEMBER_LEVEL_ERROR("2003", "会员层级不匹配"),

    /* ---------账户类编码--------- */

    ACCOUNT_NO("3000", "账户不存在"),

    ACCOUNT_AMOUNT_ERROR("3001", "账户余额不足"),

    /* ---------订单类编码--------- */

    ORDER_NO("4000", "订单不存在"),
    ORDER_ERROR("4001", "订单信息异常"),

    /* ---------网关类编码--------- */

    GATEWAY_CHANNEL_NO("5000", "网关渠道不存在"),

    ;

    private String code;
    private String memo;

}
