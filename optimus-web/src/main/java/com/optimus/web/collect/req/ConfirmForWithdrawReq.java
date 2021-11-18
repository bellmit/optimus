package com.optimus.web.collect.req;

import com.optimus.util.constants.order.OrderConfirmTypeEnum;

import lombok.Data;

/**
 * 确认提现请求
 *
 * @author hongp
 */
@Data
public class ConfirmForWithdrawReq extends BaseCollectReq {

    private static final long serialVersionUID = 4184593428540688978L;

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
