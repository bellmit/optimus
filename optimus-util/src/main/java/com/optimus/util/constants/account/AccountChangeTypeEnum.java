package com.optimus.util.constants.account;

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账户变更类型Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum AccountChangeTypeEnum {

    /* ---------账户变更类型--------- */

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

    public static AccountChangeTypeEnum instanceOf(String code) {

        for (AccountChangeTypeEnum item : AccountChangeTypeEnum.values()) {

            if (StringUtils.pathEquals(item.getCode(), code)) {
                return item;
            }

        }

        return null;

    }

}
