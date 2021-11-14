package com.optimus.util.constants.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员类型枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum MemberTypeEnum {

    /* ---------会员类型--------- */

    MEMBER_TYPE_S("S", "平台"),

    MEMBER_TYPE_M("M", "管理"),

    MEMBER_TYPE_SM("SM", "子管理"),

    MEMBER_TYPE_A("A", "代理"),

    MEMBER_TYPE_B("B", "商户"),

    MEMBER_TYPE_C("C", "码商"),

    ;

    private String code;
    private String memo;

}
