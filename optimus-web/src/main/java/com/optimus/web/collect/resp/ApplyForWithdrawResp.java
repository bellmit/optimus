package com.optimus.web.collect.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * 申请提现Resp
 *
 * @author hongp
 */
@Data
public class ApplyForWithdrawResp implements Serializable {

    private static final long serialVersionUID = 6186819433796680807L;

    /**
     * 订单编号
     */
    private String orderId;

}
