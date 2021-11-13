package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 会员代理码商关系DO
 * 
 * @author sunxp
 */
@Data
public class MemberAgentCodeRelDO implements Serializable {

    private static final long serialVersionUID = 2568884923022155298L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 下级会员编号
     */
    private String subMemberId;

    /**
     * 层级
     */
    private Integer memberLevel;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}