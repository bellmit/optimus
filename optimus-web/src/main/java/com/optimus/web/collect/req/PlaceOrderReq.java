package com.optimus.web.collect.req;

import lombok.Data;

/**
 * 下单Req
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
     * 回调商户地址[商户下单时传入]
     */
    private String merchantCallbackUrl;

    /**
     * 商户客户端IP[商户下单时传入]
     */
    private String clientIp;

    /**
     * 商户重定向地址[商户下单时传入]
     */
    private String redirectUrl;

    /**
     * 备注
     */
    private String remark;

}
