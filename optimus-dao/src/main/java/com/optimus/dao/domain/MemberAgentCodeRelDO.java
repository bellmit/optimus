package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * MemberAgentCodeRelDO
 */
@Data
@ToString
public class MemberAgentCodeRelDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String memberId;

    private String subMemberId;

    private Integer memberLevel;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}