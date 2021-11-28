package com.optimus.manager.gateway.dto;

import java.io.Serializable;

import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;
import com.optimus.util.constants.gateway.GatewayChannelStatusEnum;

import lombok.Data;

/**
 * 网关服务DTO
 * 
 * @author sunxp
 */
@Data
public class GatewayChannelDTO implements Serializable {

    private static final long serialVersionUID = -4245233461399358173L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 网关渠道编号
     */
    private String channelCode;

    /**
     * 网关渠道名称
     */
    private String channelName;

    /**
     * 网关渠道状态
     * 
     * @see GatewayChannelStatusEnum
     */
    private String channelStatus;

    /**
     * 网关渠道分组
     * 
     * @see GatewayChannelGroupEnum
     */
    private String channelGroup;

    /**
     * 网关渠道类型
     */
    private String channelType;

}
