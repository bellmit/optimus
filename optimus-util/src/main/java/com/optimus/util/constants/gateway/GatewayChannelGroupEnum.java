package com.optimus.util.constants.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 网关渠道分组枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum GatewayChannelGroupEnum {

    /* ---------网关渠道分组--------- */

    GATEWAY_CHANNEL_GROUP_I("I", "自研"),

    GATEWAY_CHANNEL_GROUP_O("O", "外部"),

    ;

    private String code;
    private String memo;

}
