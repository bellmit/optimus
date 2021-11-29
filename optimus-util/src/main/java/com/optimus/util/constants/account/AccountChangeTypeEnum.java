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

    P_PLUS("PP", "加平台内部余额户金额"),

    P_MINUS("PM", "减平台内部余额户金额"),

    B_PLUS("BP", "加余额户金额"),

    B_MINUS("BM", "减余额户金额"),

    F_PLUS("FP", "加冻结户金额"),

    F_MINUS("FM", "减冻结户金额"),

    A_PLUS("AP", "加预付款户金额"),

    A_MINUS("AM", "减预付款户金额"),

    E_PLUS("EP", "加收益户金额"),

    E_MINUS("EM", "减收益户金额"),

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
