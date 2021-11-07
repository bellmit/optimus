package com.optimus.util.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RespCodeEnum
 */
@Getter
@AllArgsConstructor
public enum RespCodeEnum {

    /* ---------系统类编码--------- */

    SUCCESS("1000", "成功"), //
    FAILE("1001", "失败"), //
    INVALID_PARAM("1002", "无效参数"), //
    ERROR_CONVERT("1003", "转换异常"), //

    ;

    private String code;
    private String memo;

}
