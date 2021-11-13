package com.optimus.util.constants.gateway;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 网关渠道支持的面额类型枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum GatewayFaceValueTypeEnum {

    /* ---------网关渠道支持的面额类型--------- */

    GATEWAY_FACE_VALUE_TYPE_F("F", "固定"),

    GATEWAY_FACE_VALUE_TYPE_S("S", "范围"),

    ;

    private String code;
    private String memo;

}
