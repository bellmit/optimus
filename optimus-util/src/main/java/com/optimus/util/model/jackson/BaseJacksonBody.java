package com.optimus.util.model.jackson;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * Jackson基础体
 * 
 * @author sunxp
 */
@Data
public class BaseJacksonBody implements Serializable {

    private static final long serialVersionUID = 8243510328848657887L;

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

    /**
     * 会员编号
     */
    private String memberId;

}
