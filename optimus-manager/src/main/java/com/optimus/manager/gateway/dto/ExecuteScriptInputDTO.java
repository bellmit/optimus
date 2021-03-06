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
     * 会员编号[商户会员编号]
     */
    private String memberId;

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
     * 实现路径
     */
    private String implPath;

    /**
     * 平台回调域名[商户下单时从系统配置中获取]
     */
    private String domainName;

    /**
     * 业务大字段[网关子渠道表存储的信息]
     */
    private String bizContent;

    /**
     * 参数[渠道回调时传入的参数]
     */
    private String args;

    /**
     * 商户客户端IP[商户下单时传入]
     */
    private String clientIp;

    /**
     * 商户重定向地址[商户下单时传入]
     */
    private String redirectUrl;

}
