package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 系统配置DO
 * 
 * @author sunxp
 */
@Data
public class CommonSystemConfigDO implements Serializable {

    private static final long serialVersionUID = -4964796267082044767L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 键
     */
    private String baseKey;

    /**
     * 值
     */
    private String value;

    /**
     * 备注
     */
    private String remark;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}