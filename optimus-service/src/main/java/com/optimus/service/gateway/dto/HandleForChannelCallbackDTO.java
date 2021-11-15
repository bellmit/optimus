package com.optimus.service.gateway.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 渠道回调处理DTO
 * 
 * @author sunxp
 */
@Data
public class HandleForChannelCallbackDTO implements Serializable {

    private static final long serialVersionUID = -265856734735774963L;

    /**
     * IP
     */
    private String ip;

    /**
     * 渠道消息
     */
    private String message;

    /**
     * 实现类型
     */
    private String implType;

    /**
     * 实现路径
     */
    private String implPath;

    /**
     * 网关渠道回调IP
     */
    private String callbackIp;

    /**
     * 业务大字段
     */
    private String bizContent;

}
