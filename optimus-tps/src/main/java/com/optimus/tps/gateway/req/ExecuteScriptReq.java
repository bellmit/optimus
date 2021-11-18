package com.optimus.tps.gateway.req;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 执行脚本Req
 * 
 * @author sunxp
 */
@Data
public class ExecuteScriptReq implements Serializable {

    private static final long serialVersionUID = 7393413432717045888L;

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
     * 实现类型
     */
    private String implType;

    /**
     * 实现路径
     */
    private String implPath;

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
