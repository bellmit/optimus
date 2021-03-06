package com.optimus.web.collect.req;

import com.optimus.util.constants.order.OrderTransferTypeEnum;

import lombok.Data;

/**
 * 划账Req
 *
 * @author hongp
 */
@Data
public class TransferReq extends BaseCollectReq {

    private static final long serialVersionUID = -6820823098344909502L;

    /**
     * 划账类型
     *
     * @see OrderTransferTypeEnum
     */
    private String transferType;

}
