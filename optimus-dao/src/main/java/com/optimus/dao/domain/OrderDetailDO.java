package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * OrderDetailDO
 */
@Data
@ToString
public class OrderDetailDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String memberId;

    private String orderId;

    private BigDecimal amount;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}