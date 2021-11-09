package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * OrderEnum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum OrderEnum {

    //
    ORDER_TYPE_R("R", "充值"),
    //
    ORDER_TYPE_W("W", "提现"),
    //
    ORDER_TYPE_M("M", "划账"),
    //
    ORDER_TYPE_C("C", "下单"),

    //
    ORDER_STATUS_NP("NP", "等待支付(->HU/AP/OF)"),
    //
    ORDER_STATUS_HU("HU", "订单挂起(->AP)"),
    //
    ORDER_STATUS_AP("AP", "订单成功"),
    //
    ORDER_STATUS_AF("AF", "订单失败"),

    //
    ORDER_SPLIT_PROFIT_STATUS_N("N", "待分润"),
    //
    ORDER_SPLIT_PROFIT_STATUS_P("P", "分润中"),
    //
    ORDER_SPLIT_PROFIT_STATUS_Y("Y", "已分润"),

    //
    ORDER_BEHAVIOR_S("S", "系统"),
    //
    ORDER_BEHAVIOR_A("A", "人为"),

    //
    ORDER_MERCHANT_NOTIFY_STATUS_NN("NN", "未通知"),
    //
    ORDER_MERCHANT_NOTIFY_STATUS_NS("NS", "通知成功"),
    //
    ORDER_MERCHANT_NOTIFY_STATUS_NF("NF", "通知失败"),

    ;

    private String code;
    private String memo;

}
