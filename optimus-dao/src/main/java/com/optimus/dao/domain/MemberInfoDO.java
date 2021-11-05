package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * MemberInfoDO
 */
@Data
@ToString
public class MemberInfoDO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String memberId;

    private String loginName;

    private String loginPassward;

    private String memberKey;

    private String merchantKey;

    private String mobile;

    private String email;

    private String showName;

    private String memberType;

    private String memberStatus;

    private String supDirectMemberId;

    private String deleteFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}