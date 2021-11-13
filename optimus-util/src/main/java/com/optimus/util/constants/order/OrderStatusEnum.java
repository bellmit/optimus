package com.optimus.util.constants.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    /* ---------订单状态--------- */

    ORDER_STATUS_NP("NP", "等待支付(->HU/AP/OF)"),

    ORDER_STATUS_HU("HU", "订单挂起(->AP)"),

    ORDER_STATUS_AP("AP", "订单成功"),

    ORDER_STATUS_AF("AF", "订单失败"),

    ;

    private String code;
    private String memo;

}
