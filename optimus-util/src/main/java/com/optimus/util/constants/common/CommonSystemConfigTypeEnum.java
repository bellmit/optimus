package com.optimus.util.constants.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统配置类型Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum CommonSystemConfigTypeEnum {

    /* ---------系统配置类型--------- */

    TYPE_S("S", "系统"),

    TYPE_BB("BB", "基础业务"),

    TYPE_MTC("MTC", "通用会员交易限制"),

    ;

    private String code;
    private String memo;
}
