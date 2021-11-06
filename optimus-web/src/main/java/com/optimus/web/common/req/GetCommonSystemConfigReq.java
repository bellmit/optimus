package com.optimus.web.common.req;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * GetCommonSystemConfigReq
 */
@Data
@ToString
public class GetCommonSystemConfigReq implements Serializable {

    private static final long serialVersionUID = 1L;

    private String key;

}
