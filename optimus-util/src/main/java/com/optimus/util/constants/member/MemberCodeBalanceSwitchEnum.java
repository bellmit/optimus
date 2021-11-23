package com.optimus.util.constants.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 码商余额限制开关Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum MemberCodeBalanceSwitchEnum {

    /* ---------码商余额限制开关--------- */

    CODE_BALANCE_SWITCH_Y("Y", "开启"),

    CODE_BALANCE_SWITCH_N("N", "关闭"),

    ;

    private String code;
    private String memo;

}
