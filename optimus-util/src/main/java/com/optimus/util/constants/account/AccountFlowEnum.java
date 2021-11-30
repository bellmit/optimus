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

    FLOW_I("I", "P", "收入"),

    FLOW_S("S", "M", "支出"),

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
