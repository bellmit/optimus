package com.optimus.web.account.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * 申请充值结果信息
 *
 * @author hongp
 */
@Data
public class ApplyForRechargeResp implements Serializable {

    private static final long serialVersionUID = -8146341715041755066L;

    /**
     * 订单号
     */
    private String orderId;
}
