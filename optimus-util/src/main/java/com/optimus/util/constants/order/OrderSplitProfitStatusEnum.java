package com.optimus.util.constants.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单分润状态Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum OrderSplitProfitStatusEnum {

    /* ---------订单分润状态--------- */

    ORDER_SPLIT_PROFIT_STATUS_N("N", "待分润"),

    ORDER_SPLIT_PROFIT_STATUS_P("P", "分润中"),

    ORDER_SPLIT_PROFIT_STATUS_Y("Y", "已分润"),

    ;

    private String code;
    private String memo;

}
