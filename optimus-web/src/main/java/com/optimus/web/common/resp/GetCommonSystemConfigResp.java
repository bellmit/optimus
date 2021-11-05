package com.optimus.web.common.resp;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * GetCommonSystemConfigResp
 */
@Data
@ToString
public class GetCommonSystemConfigResp implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;

}
