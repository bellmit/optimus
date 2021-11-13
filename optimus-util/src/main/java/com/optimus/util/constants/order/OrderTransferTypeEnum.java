package com.optimus.util.constants.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单划账类型枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum OrderTransferTypeEnum {

    /* ---------订单划账类型--------- */

    ORDER_TRANSFER_TYPE_B2A("BA", "余额户到预付款户"),

    ORDER_TRANSFER_TYPE_A2B("AB", "预付款户到余额户"),

    ;

    private String code;
    private String memo;

}
