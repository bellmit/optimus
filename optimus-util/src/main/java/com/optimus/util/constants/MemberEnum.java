package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MemberEnum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum MemberEnum {

    /* ---------会员类型--------- */

    MEMBER_TYPE_S("S", "平台"),

    MEMBER_TYPE_M("M", "管理"),

    MEMBER_TYPE_SM("SM", "子管理"),

    MEMBER_TYPE_A("A", "代理"),

    MEMBER_TYPE_C("C", "码商"),

    /* ---------会员状态--------- */

    MEMBER_STATUS_Y("Y", "有效"),

    MEMBER_STATUS_N("N", "无效"),

    /* ---------会员删除标识--------- */

    DELETE_FLAG_AD("AD", "已删除"),

    DELETE_FLAG_ND("ND", "未删除"),

    /* ---------会员-商户下单开关--------- */

    MERCHANT_ORDER_SWITCH_Y("Y", "开启"),

    MERCHANT_ORDER_SWITCH_N("N", "关闭"),

    /* ---------会员提现手续费开关--------- */

    WITHDRAW_FEE_SWITCH_Y("Y", "开启"),

    WITHDRAW_FEE_SWITCH_N("N", "关闭"),

    /* ---------会员冻结余额开关--------- */

    FREEZE_BALANCE_SWITCH_Y("Y", "开启"),

    FREEZE_BALANCE_SWITCH_N("N", "关闭"),

    /* ---------会员收取手续费类型--------- */

    COLLECT_FEE_TYPE_S("S", "单笔"),

    COLLECT_FEE_TYPE_R("R", "比例"),

    COLLECT_FEE_TYPE_SR("SR", "单笔+比例"),

    /* ---------会员手续费收取方式--------- */

    COLLECT_FEE_WAY_B("B", "余额"),

    COLLECT_FEE_WAY_A("A", "到账余额"),

    ;

    private String code;
    private String memo;

}
