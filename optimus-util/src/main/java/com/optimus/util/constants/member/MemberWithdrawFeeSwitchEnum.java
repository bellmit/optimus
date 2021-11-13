package com.optimus.util.constants.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员提现手续费开关枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum MemberWithdrawFeeSwitchEnum {

    /* ---------会员提现手续费开关--------- */

    WITHDRAW_FEE_SWITCH_Y("Y", "开启"),

    WITHDRAW_FEE_SWITCH_N("N", "关闭"),

    ;

    private String code;
    private String memo;

}
