package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CommonSystemConfigDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String baseKey;

    private String value;

    private String remark;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}