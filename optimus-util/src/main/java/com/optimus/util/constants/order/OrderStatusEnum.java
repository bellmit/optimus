package com.optimus.util.constants.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    /* ---------订单状态--------- */

    ORDER_STATUS_NP("NP", "等待支付"),

    ORDER_STATUS_AP("AP", "订单成功"),

    ORDER_STATUS_AF("AF", "订单失败"),

    ORDER_STATUS_AC("AC", "订单关闭"),

    ;

    private String code;
    private String memo;

}
