package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应码Enum
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

    ERROR_CONFIG("1003", "配置异常"),

    ERROR_CONVERT("1004", "转换异常"),

    ERROR_SIGN("1005", "签名异常"),

    ERROR_LIMIT("1006", "限流异常"),

    ERROR_IP("1007", "IP异常"),

    /* ---------会员类编码--------- */

    MEMBER_ERROR("2000", "会员信息异常"),

    MEMBER_CHANNEL_ERROR("2001", "会员渠道异常"),

    MEMBER_TRANS_PERMISSION_ERROR("2002", "会员交易限制异常"),

    /* ---------账户类编码--------- */

    ACCOUNT_ERROR("3000", "账户信息异常"),

    ACCOUNT_TRANSACTION_ERROR("3001", "账户交易异常"),

    /* ---------订单类编码--------- */

    ORDER_ERROR("4000", "订单信息异常"),

    ORDER_PLACE_ERROR("4001", "下单异常"),

    /* ---------网关类编码--------- */

    GATEWAY_CHANNEL_ERROR("5000", "网关渠道信息异常"),

    GATEWAY_EXECUTE_SCRIPT_ERROR("5001", "网关渠道脚本执行异常"),

    ;

    private String code;
    private String memo;

}
