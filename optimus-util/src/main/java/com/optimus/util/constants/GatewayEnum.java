package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * GatewayEnum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum GatewayEnum {

    //
    GATEWAY_CHANNEL_STATUS_Y("Y", "启用"),
    //
    GATEWAY_CHANNEL_STATUS_N("N", "禁用"),

    //
    GATEWAY_CHANNEL_GROUP_I("I", "自研"),
    //
    GATEWAY_CHANNEL_GROUP_O("O", "外部"),

    //
    GATEWAY_CHANNEL_TYPE_ALIPAY("ALIPAY", "支付宝"),
    //
    GATEWAY_CHANNEL_TYPE_WECHAT("WECHAT", "微信"),
    //
    GATEWAY_CHANNEL_TYPE_OC("OC", "油卡"),
    //
    GATEWAY_CHANNEL_TYPE_TB("TB", "话费"),

    //
    GATEWAY_FACE_VALUE_TYPE_F("F", "固定"),
    //
    GATEWAY_FACE_VALUE_TYPE_S("S", "范围"),

    ;

    private String code;
    private String memo;

}
