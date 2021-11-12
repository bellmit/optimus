package com.optimus.web.collect.req;

import lombok.Data;

/**
 * 提现请求
 *
 * @author hongp
 */
@Data
public class WithdrawReq extends BaseCollectReq {

    private static final long serialVersionUID = -2935475925686536698L;

    /**
     * 直接下级会员编号
     */
    private String subDirectMemberId;

}
