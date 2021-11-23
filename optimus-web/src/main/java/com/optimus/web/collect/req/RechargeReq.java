package com.optimus.web.collect.req;

import lombok.Data;

/**
 * 充值Req
 *
 * @author hongp
 */
@Data
public class RechargeReq extends BaseCollectReq {

    private static final long serialVersionUID = -7302042340018422438L;

    /**
     * 直接下级会员编号
     */
    private String subDirectMemberId;

}
