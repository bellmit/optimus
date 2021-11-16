package com.optimus.manager.gateway.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * 渠道消息输入DTO
 * 
 * @author sunxp
 */
@Data
public class InputChannelMessageDTO implements Serializable {

    private static final long serialVersionUID = -265856734735774963L;

    /**
     * 实现类型
     */
    private String implType;

    /**
     * 实现路径
     */
    private String implPath;

    /**
     * 业务大字段
     */
    private String bizContent;

    /**
     * 渠道消息
     */
    private String message;

}
