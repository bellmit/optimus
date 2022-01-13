package com.optimus.dao.query;

import java.io.Serializable;

import com.optimus.util.constants.gateway.GatewayChannelStatusEnum;

import lombok.Data;

/**
 * 网关子渠道Query
 * 
 * @author sunxp
 */
@Data
public class GatewaySubChannelQuery implements Serializable {

    private static final long serialVersionUID = 807600634893322056L;

    /**
     * 父渠道编号
     */
    private String parentChannelCode;

    /**
     * 网关子渠道状态
     * 
     * @see GatewayChannelStatusEnum
     */
    private String channelStatus;

}
