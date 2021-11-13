package com.optimus.util.constants.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员冻结余额开关枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum MemberFreezeBalanceSwitchEnum {

    /* ---------会员冻结余额开关--------- */

    FREEZE_BALANCE_SWITCH_Y("Y", "开启"),

    FREEZE_BALANCE_SWITCH_N("N", "关闭"),

    ;

    private String code;
    private String memo;

}
