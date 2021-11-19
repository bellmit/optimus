package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ScriptEnum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum ScriptEnum {

    /* ---------脚本方法类--------- */

    CREATE("create", "平台调用网关渠道下单方法"),

    PARSE("parse", "网关渠道回调平台解析参数方法"),

    QUERY("query", "平台调用网关渠道查询订单方法"),

    ;

    private String code;
    private String memo;

}