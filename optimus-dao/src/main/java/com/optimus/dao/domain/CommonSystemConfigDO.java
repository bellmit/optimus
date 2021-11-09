package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * CommonSystemConfigDO
 * 
 * @author sunxp
 */
@Data
public class CommonSystemConfigDO implements Serializable {

    private static final long serialVersionUID = -4964796267082044767L;

    private Long id;

    private String baseKey;

    private String value;

    private String remark;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}