package com.optimus.web.common.req;

import java.util.Date;

import com.optimus.util.req.Req;

import groovy.transform.EqualsAndHashCode;
import lombok.Data;

/**
 * GetCommonSystemConfigReq
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetCommonSystemConfigReq extends Req {

    private static final long serialVersionUID = 7560553439812942745L;

    private String baseKey;

    private Date date;

}
