package com.optimus.util.constants.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单确认类型枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum OrderConfirmTypeEnum {

    /* ---------订单确认类型--------- */

    ORDER_CONFIRM_TYPE_P("P", "通过"),

    ORDER_CONFIRM_TYPE_R("R", "驳回"),

    ;

    private String code;
    private String memo;

}
