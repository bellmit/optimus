package com.optimus.web.common.resp;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * GetCommonSystemConfigResp
 */
@Data
public class GetCommonSystemConfigResp implements Serializable {

    private static final long serialVersionUID = -4806554998121629152L;

    private String value;

    private Date date;

}
