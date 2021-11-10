package com.optimus.web.collect.req;

import com.optimus.util.req.Req;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 申请充值请求
 *
 * @author hongp
 */
@Data
public class ApplyForRechargeReq extends Req {

    private static final long serialVersionUID = 7078100127511156222L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 金额
     */
    private BigDecimal amount;
}
