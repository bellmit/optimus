package com.optimus.web.common.req;

import com.optimus.util.req.Req;

import lombok.Data;
import lombok.ToString;

/**
 * GetCommonSystemConfigReq
 * 
 * @author sunxp
 */
@Data
@ToString
public class GetCommonSystemConfigReq extends Req {

    private static final long serialVersionUID = 1L;

    private String key;

}
