package com.optimus.util.exception;

import com.optimus.util.constants.RespCodeEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * OptimusException
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OptimusException extends RuntimeException {

    private static final long serialVersionUID = 7068851877027924285L;

    private RespCodeEnum respCodeEnum;

    private String memo;

    public OptimusException(RespCodeEnum respCodeEnum) {
        this.respCodeEnum = respCodeEnum;
    }

}