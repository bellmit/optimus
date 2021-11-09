package com.optimus.service.account.repuest;

import com.optimus.util.req.Req;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 申请充值请求
 *
 * @author hongp
 */
@Data
public class ApplyForRechargeRequest extends Req {

    private static final long serialVersionUID = 98560750616070655L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 金额
     */
    private BigDecimal amount;
}
