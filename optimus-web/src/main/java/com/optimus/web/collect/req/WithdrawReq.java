package com.optimus.web.collect.req;

import com.optimus.util.req.Req;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 提现请求
 *
 * @author hongp
 */
@Data
public class WithdrawReq extends Req {

    private static final long serialVersionUID = -2935475925686536698L;

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
