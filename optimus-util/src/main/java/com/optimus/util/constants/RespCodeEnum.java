package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RespCodeEnum
 */
@Getter
@AllArgsConstructor
public enum RespCodeEnum {

    /* ---------系统类编码--------- */

    SUCCESS("1000", "成功"), //
    FAILE("1001", "失败"), //
    INVALID_PARAM("1002", "无效参数"), //
    ERROR_CONVERT("1003", "转换异常"), //
    ERROR_SIGN("1004", "签名异常"), //

    /* ---------会员类编码--------- */

    MEMBER_NO("2000", "会员不存在"), //
    MEMBER_ERROR("2001", "会员信息异常"), //

    /* ---------账户类编码--------- */

    ACCOUNT_NO("3000", "账户不存在"), //

    /* ---------订单类编码--------- */

    ORDER_NO("4000", "订单不存在"), //

    /* ---------网关类编码--------- */

    GATEWAY_CHANNEL_NO("5000", "网关渠道不存在"), //

    ;

    private String code;
    private String memo;

}
