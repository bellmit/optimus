package com.optimus.util.constants.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账户流枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum AccountFlowEnum {

    /* ---------账户流--------- */

    ACCOUNT_FLOW_I("I", "收入"),

    ACCOUNT_FLOW_S("S", "支出"),

    ;

    private String code;
    private String memo;

}
