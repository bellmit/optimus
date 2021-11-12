package com.optimus.web.collect.req;

import com.optimus.util.req.Req;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 收单基础请求
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
