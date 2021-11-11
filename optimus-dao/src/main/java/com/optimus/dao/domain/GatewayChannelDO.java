package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 网关渠道DO
 * 
 * @author sunxp
 */
@Data
public class GatewayChannelDO implements Serializable {

    private static final long serialVersionUID = -7977533896868875306L;

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