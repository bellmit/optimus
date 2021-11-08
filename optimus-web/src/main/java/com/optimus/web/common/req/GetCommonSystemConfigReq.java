package com.optimus.web.common.req;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * GetCommonSystemConfigReq
 */
@Data
@ToString
public class GetCommonSystemConfigReq implements Serializable {

    private static final long serialVersionUID = -7003794914303013777L;

    private String baseKey;

    private Date date;

}
