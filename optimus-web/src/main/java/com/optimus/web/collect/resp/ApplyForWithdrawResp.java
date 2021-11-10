package com.optimus.web.collect.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * 申请提现结果信息
 *
 * @author hongp
 */
@Data
public class ApplyForWithdrawResp implements Serializable {

    private static final long serialVersionUID = 6186819433796680807L;

    /**
     * 订单号
     */
    private String orderId;

}
