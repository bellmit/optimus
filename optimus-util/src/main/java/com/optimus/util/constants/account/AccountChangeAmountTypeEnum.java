package com.optimus.util.constants.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AccountChangeAmountTypeEnum
 */
@Getter
@AllArgsConstructor
public enum AccountChangeAmountTypeEnum {

    P_PLUS("P+", "加平台内部余额户金额"), //
    P_MINUS("P-", "减平台内部余额户金额"), //

    B_PLUS("B+", "加余额户金额"), //
    B_MINUS("B-", "减余额户金额"), //

    F_PLUS("F+", "加冻结户金额"), //
    F_MINUS("F-", "减冻结户金额"), //

    A_PLUS("A+", "加预付款户金额"), //
    A_MINUS("A-", "减预付款户金额"), //

    E_PLUS("E+", "加收益户金额"), //
    E_MINUS("E-", "减收益户金额"), //

    ;

    private String code;
    private String memo;

}
