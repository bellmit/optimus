package com.optimus.dao.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

/**
 * MemberChannelDO
 */
@Data
public class MemberChannelDO implements Serializable {

    private static final long serialVersionUID = -716903795015284362L;

    private Long id;

    private String memberId;

    private String channelCode;

    private String subChannelCode;

    private BigDecimal rate;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}