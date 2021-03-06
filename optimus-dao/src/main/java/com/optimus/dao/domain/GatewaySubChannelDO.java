package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import com.optimus.util.constants.gateway.GatewayChannelStatusEnum;
import com.optimus.util.constants.gateway.GatewayFaceValueTypeEnum;

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
     * 
     * {"channelMerchantId":"商户编号","channelMerchantKey":"商户密钥","channelCode":"真实渠道编号","callbackUrl":"回调地址","redirectUrl":"重定向地址","createOrderUrl":"创建订单地址","queryOrderUrl":"调单查询地址"}
     */
    private String bizContent;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}