package com.optimus.service.account.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充值请求
 *
 * @author hongp
 */
@Data
public class RechargeDTO implements Serializable {

    private static final long serialVersionUID = 5659248964576182912L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 订单号
     */
    private BigDecimal orderId;
}
