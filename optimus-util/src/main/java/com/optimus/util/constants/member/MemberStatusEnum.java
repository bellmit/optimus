package com.optimus.util.constants.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员状态Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum MemberStatusEnum {

    /* ---------会员状态--------- */

    MEMBER_STATUS_Y("Y", "有效"),

    MEMBER_STATUS_N("N", "无效"),

    ;

    private String code;
    private String memo;

}
