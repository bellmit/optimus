package com.optimus.util.model.exception;

import com.optimus.util.constants.RespCodeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OptimusException
 * 
 * @author sunxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptimusException extends RuntimeException {

    private static final long serialVersionUID = 7068851877027924285L;

    /**
     * 响应状态码枚举
     */
    private RespCodeEnum respCodeEnum;

    /**
     * 附加描述
     */
    private String memo;

    public OptimusException(RespCodeEnum respCodeEnum) {
        this.respCodeEnum = respCodeEnum;
    }

}
