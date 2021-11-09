package com.optimus.util.resp;

import java.io.Serializable;

import com.optimus.util.constants.RespCodeEnum;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Resp<T>
 * 
 * @author sunxp
 */
@Data
@NoArgsConstructor
@ToString
public class Resp<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code = RespCodeEnum.SUCCESS.getCode();

    private String memo = RespCodeEnum.SUCCESS.getMemo();

    private T data;

    public Resp(T data) {
        this.data = data;
    }

}
