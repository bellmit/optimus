package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import com.optimus.util.constants.GatewayEnum;
import lombok.Data;

/**
 * 网关子渠道DO
 * 
 * @author sunxp
 */
@Data
public class GatewaySubChannelDO implements Serializable {

    private static final long serialVersionUID = 9126865979674298686L;

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
     * 网关子渠道编号
     */
    private String channelName;

    /**
     * 网关子渠道状态
     * 
     * @see GatewayEnum
     */
    private String channelStatus;

    /**
     * 面额类型
     * 
     * @see GatewayEnum
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

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}