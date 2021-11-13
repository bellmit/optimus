package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 订单详情DO
 * 
 * @author sunxp
 */
@Data
public class OrderDetailDO implements Serializable {

    private static final long serialVersionUID = -1589077009756433789L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 金额
     */
    private BigDecimal amount;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}