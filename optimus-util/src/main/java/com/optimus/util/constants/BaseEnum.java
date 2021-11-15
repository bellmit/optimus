package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * BaseEnum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum BaseEnum {

    /* ---------基础类--------- */

    UNKNOWN("unknown", "未知"),

    ;

    private String code;
    private String memo;

}
