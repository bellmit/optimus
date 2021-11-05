package com.optimus.util.constants;

/**
 * RespCodeEnum
 */
public enum RespCodeEnum {

    /* ---------系统类编码--------- */

    SUCCESS("1000", "成功"), //
    FAILE("1001", "失败"), //

    /* ---------会员类编码--------- */

    MEMBER_NO("2000", "会员不存在"), //

    /* ---------账户类编码--------- */

    ACCOUNT_NO("3000", "账户不存在"), //

    /* ---------订单类编码--------- */

    ORDER_NO("4000", "订单不存在"), //

    /* ---------网关类编码--------- */

    GATEWAY_CHANNEL_NO("5000", "网关渠道不存在"), //

    ;

    private String code;
    private String memo;

    private RespCodeEnum(String code, String memo) {
        this.code = code;
        this.memo = memo;
    }

    public String getCode() {
        return code;
    }

    public String getMemo() {
        return memo;
    }

}
