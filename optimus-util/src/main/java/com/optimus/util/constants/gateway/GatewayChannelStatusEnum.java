package com.optimus.util.constants.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 网关渠道状态Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum GatewayChannelStatusEnum {

    /* ---------网关渠道状态--------- */

    GATEWAY_CHANNEL_STATUS_Y("Y", "启用"),

    GATEWAY_CHANNEL_STATUS_N("N", "禁用"),

    ;

    private String code;
    private String memo;

}
