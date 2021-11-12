package com.optimus.util.resp;

import java.io.Serializable;

import com.optimus.util.constants.RespCodeEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resp<T>
 * 
 * @author sunxp
 */
@Data
@NoArgsConstructor
public class Resp<T> implements Serializable {

    private static final long serialVersionUID = -5107305879027514980L;

    private String code = RespCodeEnum.SUCCESS.getCode();

    private String memo = RespCodeEnum.SUCCESS.getMemo();

    /**
     * 响应报文体
     */
    private T data;

    public Resp(T data) {
        this.data = data;
    }

}
