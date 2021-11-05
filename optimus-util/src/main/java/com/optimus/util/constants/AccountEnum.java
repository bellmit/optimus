package com.optimus.util.constants;

/**
 * AccountEnum
 */
public enum AccountEnum {

    ACCOUNT_TYPE_P("P", "平台内部余额户(平台)"), //
    ACCOUNT_TYPE_B("B", "余额户(管理/代理/商户/码商)"), //
    ACCOUNT_TYPE_F("F", "冻结户(商户/码商)"), //
    ACCOUNT_TYPE_A("A", "预付款户(管理/代理)"), //
    ACCOUNT_TYPE_E("E", "收益户(代理)"), //

    ACCOUNT_FLOW_I("I", "收入"), //
    ACCOUNT_FLOW_S("S", "支出"), //

    // CHANGE_TYPE 变更类型待补充

    ;

    private String code;
    private String memo;

    private AccountEnum(String code, String memo) {
        this.code = code;
        this.memo = memo;
    }

    public String getCode() {
        return code;
    }

    public String getMemo() {
        return memo;
    }

}
