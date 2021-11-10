package com.optimus.web.collect.req;

import com.optimus.util.req.Req;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 划账请求
 *
 * @author hongp
 */
@Data
public class TransferReq extends Req {

    private static final long serialVersionUID = -6820823098344909502L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 金额
     */
    private BigDecimal amount;
}
