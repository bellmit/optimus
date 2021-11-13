package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * 会员渠道DO
 * 
 * @author sunxp
 */
@Data
public class MemberChannelDO implements Serializable {

    private static final long serialVersionUID = -716903795015284362L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 网关渠道编号
     */
    private String channelCode;

    /**
     * 网关子渠道编号
     */
    private String subChannelCode;

    /**
     * 费率
     */
    private BigDecimal rate;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}