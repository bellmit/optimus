package com.optimus.util.req;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * Req
 * 
 * @author sunxp
 */
@Data
public class Req implements Serializable {

    private static final long serialVersionUID = 1241445301028911388L;

    /**
     * 方法名称
     */
    private String method;

    /**
     * 时间戳
     * 
     * yyyy-MM-dd HH:mm:ss
     */
    private Date timestamp;

    /**
     * 签名
     */
    private String sign;

}
