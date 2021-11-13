package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * OrderEnum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum OrderEnum {

    /* ---------订单类型--------- */

    ORDER_TYPE_R("R", "充值"),

    ORDER_TYPE_W("W", "提现"),

    ORDER_TYPE_M("M", "划账"),

    ORDER_TYPE_C("C", "下单"),

    /* ---------订单状态--------- */

    ORDER_STATUS_NP("NP", "等待支付(->HU/AP/OF)"),

    ORDER_STATUS_HU("HU", "订单挂起(->AP)"),

    ORDER_STATUS_AP("AP", "订单成功"),

    ORDER_STATUS_AF("AF", "订单失败"),

    /* ---------订单分润状态--------- */

    ORDER_SPLIT_PROFIT_STATUS_N("N", "待分润"),

    ORDER_SPLIT_PROFIT_STATUS_P("P", "分润中"),

    ORDER_SPLIT_PROFIT_STATUS_Y("Y", "已分润"),

    /* ---------订单行为--------- */

    ORDER_BEHAVIOR_S("S", "系统"),

    ORDER_BEHAVIOR_A("A", "人为"),

    /* ---------订单商户通知状态--------- */

    ORDER_MERCHANT_NOTIFY_STATUS_NN("NN", "未通知"),

    ORDER_MERCHANT_NOTIFY_STATUS_NS("NS", "通知成功"),

    ORDER_MERCHANT_NOTIFY_STATUS_NF("NF", "通知失败"),

    /* ---------订单划账类型--------- */

    ORDER_TRANSFER_TYPE_B2A("BA", "余额户到预付款户"),

    ORDER_TRANSFER_TYPE_A2B("AB", "预付款户到余额户"),

    ;

    private String code;
    private String memo;

    private static final Map<String, OrderEnum> valueMap = new HashMap<>(OrderEnum.values().length);

    public static  OrderEnum valueOfType(String type) {
        return valueMap.get(type);
    }

}
