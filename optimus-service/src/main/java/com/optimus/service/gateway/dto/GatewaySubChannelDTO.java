package com.optimus.service.gateway.dto;

import java.io.Serializable;

import com.optimus.util.constants.gateway.GatewayChannelStatusEnum;
import com.optimus.util.constants.gateway.GatewayFaceValueTypeEnum;

import lombok.Data;

/**
 * 子渠道DTO
 * 
 * @author sunxp
 */
@Data
public class GatewaySubChannelDTO implements Serializable {

    private static final long serialVersionUID = -5313332376577888327L;

    /**
     * 父渠道编号
     */
    private String parentChannelCode;

    /**
     * 网关子渠道编号
     */
    private String channelCode;

    /**
     * 网关子渠道编号
     */
    private String channelName;

    /**
     * 网关子渠道状态
     * 
     * @see GatewayChannelStatusEnum
     */
    private String channelStatus;

    /**
     * 面额类型
     * 
     * @see GatewayFaceValueTypeEnum
     */
    private String faceValueType;

    /**
     * 面额值
     */
    private String faceValue;

    /**
     * 权重
     */
    private Short weight;

    /**
     * 疲劳度
     */
    private Short fatigue;

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
