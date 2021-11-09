package com.optimus.web.common.resp;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * GetCommonSystemConfigResp
 * 
 * @author sunxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetCommonSystemConfigResp implements Serializable {

    private static final long serialVersionUID = 1L;

    private String value;

}
