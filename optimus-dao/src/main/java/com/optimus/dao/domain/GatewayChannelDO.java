package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * GatewayChannelDO
 */
@Data
@ToString
public class GatewayChannelDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String channelCode;

    private String channelName;

    private String channelStatus;

    private String channelGroup;

    private String channelType;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}