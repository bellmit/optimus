package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RespCodeEnum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum RespCodeEnum {

    /* ---------系统类编码--------- */

    // 成功
    SUCCESS("1000", "成功"),
    // 失败
    FAILE("1001", "失败"),
    // 无效参数
    INVALID_PARAM("1002", "无效参数"),
    // 转换异常
    ERROR_CONVERT("1003", "转换异常"),
    // 签名异常
    ERROR_SIGN("1004", "签名异常"),

    ;

    private String code;
    private String memo;

}
