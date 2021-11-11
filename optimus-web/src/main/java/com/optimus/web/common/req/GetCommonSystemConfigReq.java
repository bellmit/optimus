package com.optimus.web.common.req;

import com.optimus.util.req.Req;

import lombok.Data;

/**
 * GetCommonSystemConfigReq
 * 
 * @author sunxp
 */
@Data
public class GetCommonSystemConfigReq extends Req {

    private static final long serialVersionUID = 7560553439812942745L;

    private String baseKey;

}
