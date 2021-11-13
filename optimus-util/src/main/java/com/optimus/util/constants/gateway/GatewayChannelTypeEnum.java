package com.optimus.util.constants.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 网关渠道类型枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum GatewayChannelTypeEnum {

    /* ---------网关渠道类型--------- */

    GATEWAY_CHANNEL_TYPE_ALIPAY("ALIPAY", "支付宝"),

    GATEWAY_CHANNEL_TYPE_WECHAT("WECHAT", "微信"),

    GATEWAY_CHANNEL_TYPE_OC("OC", "油卡"),

    GATEWAY_CHANNEL_TYPE_TB("TB", "话费"),

    ;

    private String code;
    private String memo;

}
