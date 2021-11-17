package com.optimus.manager.gateway.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 网关脚本输入DTO
 * 
 * @author sunxp
 */
@Data
public class ExecuteScriptInputDTO implements Serializable {

    private static final long serialVersionUID = -265856734735774963L;

    /**
     * 脚本方法
     */
    private String scriptMethod;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 被调用方订单编号
     */
    private String calleeOrderId;

    /**
     * 订单金额
     */
    private BigDecimal amount;

    /**
     * 订单时间
     */
    private Date orderTime;

    /**
     * 商户客户端IP
     */
    private String clientIp;

    /**
     * 商户重定向地址
     */
    private String redirectUrl;

    /**
     * 实现类型
     */
    private String implType;

    /**
     * 实现路径
     */
    private String implPath;

    /**
     * 业务大字段
     */
    private String bizContent;

    /**
     * 渠道消息
     */
    private String message;

}