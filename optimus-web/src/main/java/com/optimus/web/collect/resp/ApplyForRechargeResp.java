package com.optimus.web.collect.resp;

import lombok.Data;

import java.io.Serializable;

/**
 * 申请充值Resp
 *
 * @author hongp
 */
@Data
public class ApplyForRechargeResp implements Serializable {

    private static final long serialVersionUID = -8146341715041755066L;

    /**
     * 订单编号
     */
    private String orderId;

}
