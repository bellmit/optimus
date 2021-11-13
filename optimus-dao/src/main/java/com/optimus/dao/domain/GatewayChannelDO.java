package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import com.optimus.util.constants.gateway.GatewayChannelTypeEnum;

import lombok.Data;

/**
 * 网关渠道DO
 * 
 * @author sunxp
 */
@Data
public class GatewayChannelDO implements Serializable {

    private static final long serialVersionUID = -7977533896868875306L;

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
     */
    private String channelGroup;

    /**
     * 网关渠道类型
     * 
     * @see GatewayChannelTypeEnum
     */
    private String channelType;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}