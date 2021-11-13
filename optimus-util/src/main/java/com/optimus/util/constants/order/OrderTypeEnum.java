package com.optimus.util.constants.order;

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单类型枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum OrderTypeEnum {

    /* ---------订单类型--------- */

    ORDER_TYPE_R("R", "rechargeOrder", "充值"),

    ORDER_TYPE_W("W", "withdrawOrder", "提现"),

    ORDER_TYPE_M("M", "transferOrder", "划账"),

    ORDER_TYPE_C("C", "placeOrder", "下单"),

    ;

    private String code;
    private String instance;
    private String memo;

    public static OrderTypeEnum instanceOf(String code) {

        for (OrderTypeEnum item : OrderTypeEnum.values()) {

            if (StringUtils.pathEquals(item.getCode(), code)) {
                return item;
            }

        }

        return null;

    }

}
