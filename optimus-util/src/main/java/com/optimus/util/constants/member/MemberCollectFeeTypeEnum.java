package com.optimus.util.constants.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员收取手续费类型枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum MemberCollectFeeTypeEnum {

    /* ---------会员收取手续费类型--------- */

    COLLECT_FEE_TYPE_S("S", "单笔"),

    COLLECT_FEE_TYPE_R("R", "比例"),

    COLLECT_FEE_TYPE_SR("SR", "单笔+比例"),

    ;

    private String code;
    private String memo;

}
