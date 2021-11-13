package com.optimus.util.constants.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员删除标识枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum MemberDeleteFlagEnum {

    /* ---------会员删除标识--------- */

    DELETE_FLAG_AD("AD", "已删除"),

    DELETE_FLAG_ND("ND", "未删除"),

    ;

    private String code;
    private String memo;

}
