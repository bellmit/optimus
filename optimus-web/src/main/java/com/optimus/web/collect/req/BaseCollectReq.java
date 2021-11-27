package com.optimus.web.collect.req;

import lombok.Data;

import java.math.BigDecimal;

import com.optimus.util.model.req.Req;

/**
 * 收单基础Req
 *
 * @author hongp
 */
@Data
public class BaseCollectReq extends Req {

    private static final long serialVersionUID = 3069479259292633815L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 调用方订单编号
     */
    private String callerOrderId;

}
