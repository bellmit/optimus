package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import com.optimus.util.constants.common.CommonSystemConfigBaseKeyEnum;
import com.optimus.util.constants.common.CommonSystemConfigTypeEnum;

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
     * 类型
     * 
     * @see CommonSystemConfigTypeEnum
     */
    private String type;

    /**
     * 键
     * 
     * @see CommonSystemConfigBaseKeyEnum
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