package com.optimus.web.collect.req;

import lombok.Data;

/**
 * 下单
 * 
 * @author sunxp
 */
@Data
public class PlaceOrderReq extends BaseCollectReq {

    private static final long serialVersionUID = -2457534615535060237L;

    /**
     * 网关渠道编号
     */
    private String channelCode;

    /**
     * 商户回调地址
     */
    private String merchantCallBackUrl;

}
