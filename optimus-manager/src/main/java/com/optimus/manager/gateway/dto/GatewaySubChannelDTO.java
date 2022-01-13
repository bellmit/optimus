package com.optimus.manager.gateway.dto;

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
     * 主键
     */
    private Long id;

    /**
     * 父渠道编号
     */
    private String parentChannelCode;

    /**
     * 网关子渠道编号
     */
    private String channelCode;

    /**
     * 网关子渠道名称
     */
    private String channelName;

    /**
     * 网关子渠道状态
     * 
     * @see GatewayChannelStatusEnum
     */
    private String channelStatus;

    /**
     * 分类
     */
    private String classify;

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
     * 实现路径
     */
    private String implPath;

    /**
     * 网关渠道回调IP
     */
    private String callbackIp;

    /**
     * 业务大字段[网关子渠道表存储的信息]
     */
    private String bizContent;

}
