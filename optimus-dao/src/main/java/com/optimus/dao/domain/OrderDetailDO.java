package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * OrderDetailDO
 */
@Data
public class OrderDetailDO implements Serializable {

    private static final long serialVersionUID = -1589077009756433789L;

    private Long id;

    private String memberId;

    private String orderId;

    private BigDecimal amount;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}