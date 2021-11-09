package com.optimus.util.req;

import java.io.Serializable;

import lombok.Data;

/**
 * Req
 * 
 * @author sunxp
 */
@Data
public class Req implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7139757075670040925L;

    private String method;

    private String timestamp;

    private String sign;

}