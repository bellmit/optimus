package com.optimus.util.constants.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员商户下单开关枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum MemberMerchantOrderSwitchEnum {

    /* ---------会员商户下单开关--------- */

    MERCHANT_ORDER_SWITCH_Y("Y", "开启"),

    MERCHANT_ORDER_SWITCH_N("N", "关闭"),

    ;

    private String code;
    private String memo;

}
