package com.optimus.util.constants.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员手续费收取方式枚举
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum MemberCollectFeeWayEnum {

    /* ---------会员手续费收取方式--------- */

    COLLECT_FEE_WAY_B("B", "余额"),

    COLLECT_FEE_WAY_A("A", "到账余额"),

    ;

    private String code;
    private String memo;

}
