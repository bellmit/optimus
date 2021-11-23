package com.optimus.util.constants.account;

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账户流Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum AccountFlowEnum {

    /* ---------账户流--------- */

    ACCOUNT_FLOW_I("I", "+", "收入"),

    ACCOUNT_FLOW_S("S", "-", "支出"),

    ;

    private String code;
    private String symbol;
    private String memo;

    public static AccountFlowEnum instanceOfSymbol(String symbol) {

        for (AccountFlowEnum item : AccountFlowEnum.values()) {

            if (StringUtils.pathEquals(item.getSymbol(), symbol)) {
                return item;
            }

        }

        return null;

    }

}
