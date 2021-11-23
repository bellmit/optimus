package com.optimus.web.collect.req;

import com.optimus.util.constants.order.OrderConfirmTypeEnum;

import lombok.Data;

/**
 * 确认充值Req
 *
 * @author hongp
 */
@Data
public class ConfirmForRechargeReq extends BaseCollectReq {

    private static final long serialVersionUID = 8367300262598958078L;

    /**
     * 下级会员编号
     */
    private String subMemberId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 确认类型
     *
     * @see OrderConfirmTypeEnum
     */
    private String confirmType;

}
