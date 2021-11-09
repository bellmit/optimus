package com.optimus.web.common.req;

import java.util.Date;

import com.optimus.util.req.Req;

import lombok.Data;

/**
 * GetCommonSystemConfigReq
 */
@Data
public class GetCommonSystemConfigReq extends Req {

    private static final long serialVersionUID = 7560553439812942745L;

    private String baseKey;

    private Date date;

}
