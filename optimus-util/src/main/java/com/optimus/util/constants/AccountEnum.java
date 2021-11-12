package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AccountEnum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum AccountEnum {

    /* ---------账户类型--------- */

    ACCOUNT_TYPE_P("P", "平台内部余额户(平台)"),

    ACCOUNT_TYPE_B("B", "余额户(管理/代理/商户/码商)"),

    ACCOUNT_TYPE_F("F", "冻结户(商户/码商)"),

    ACCOUNT_TYPE_A("A", "预付款户(管理/代理)"),

    ACCOUNT_TYPE_E("E", "收益户(代理)"),

    /* ---------账户流--------- */

    ACCOUNT_FLOW_I("I", "收入"),

    ACCOUNT_FLOW_S("S", "支出"),

    /* ---------记账类型--------- */

    P_PLUS("P+", "加平台内部余额户金额"),

    P_MINUS("P-", "减平台内部余额户金额"),

    B_PLUS("B+", "加余额户金额"),

    B_MINUS("B-", "减余额户金额"),

    F_PLUS("F+", "加冻结户金额"),

    F_MINUS("F-", "减冻结户金额"),

    A_PLUS("A+", "加预付款户金额"),

    A_MINUS("A-", "减预付款户金额"),

    E_PLUS("E+", "加收益户金额"),

    E_MINUS("E-", "减收益户金额"),

    ;

    private String code;
    private String memo;

}
