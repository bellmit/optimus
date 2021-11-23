package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum BaseEnum {

    /* ---------基础类--------- */

    UNKNOWN("unknown", "未知"),

    SCALE_TWO("2", "精度2"),

    SCALE_FOUR("4", "精度4"),

    ;

    private String code;
    private String memo;

}
