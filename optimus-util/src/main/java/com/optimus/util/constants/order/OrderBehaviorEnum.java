package com.optimus.util.constants.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单行为Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum OrderBehaviorEnum {

    /* ---------订单行为--------- */

    BEHAVIOR_S("S", "系统"),

    BEHAVIOR_A("A", "人为"),

    ;

    private String code;
    private String memo;

}
