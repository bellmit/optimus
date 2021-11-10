package com.optimus.service.account.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 提现请求
 *
 * @author hongp
 */
@Data
public class WithdrawDTO implements Serializable {

    private static final long serialVersionUID = 4618877556614726401L;

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
