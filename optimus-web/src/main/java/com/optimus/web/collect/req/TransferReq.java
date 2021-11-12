package com.optimus.web.collect.req;

import lombok.Data;

/**
 * 划账请求
 *
 * @author hongp
 */
@Data
public class TransferReq extends BaseCollectReq {

    private static final long serialVersionUID = -6820823098344909502L;

    /**
     * 划账类型 type[类型：余额-预付款/预付款-余额]
     */
    private String transferType;

}
