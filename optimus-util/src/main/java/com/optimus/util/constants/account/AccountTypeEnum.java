package com.optimus.util.constants.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账户类型Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum AccountTypeEnum {

    /* ---------账户类型--------- */

    ACCOUNT_TYPE_P("P", "平台内部余额户(平台)"),

    ACCOUNT_TYPE_B("B", "余额户(管理/代理/商户/码商)"),

    ACCOUNT_TYPE_F("F", "冻结户(管理/代理/商户/码商)"),

    ACCOUNT_TYPE_A("A", "预付款户(管理/代理)"),

    ACCOUNT_TYPE_E("E", "收益户(代理)"),

    ;

    private String code;
    private String memo;

}
