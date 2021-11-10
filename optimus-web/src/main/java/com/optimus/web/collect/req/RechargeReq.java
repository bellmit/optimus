package com.optimus.web.collect.req;

import com.optimus.util.req.Req;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 充值请求
 *
 * @author hongp
 */
@Data
public class RechargeReq extends Req {

    private static final long serialVersionUID = -7302042340018422438L;

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
