package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * GatewaySubChannelDO
 */
@Data
@ToString
public class GatewaySubChannelDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String parentChannelCode;

    private String channelCode;

    private String channelName;

    private String channelStatus;

    private String faceValueType;

    private String faceValue;

    private Short weight;

    private Short fatigue;

    private Date nextUsableTime;

    private String implType;

    private String implPath;

    private String callbackIp;

    private String bizContent;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}