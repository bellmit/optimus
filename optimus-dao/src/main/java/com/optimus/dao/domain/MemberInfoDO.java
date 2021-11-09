package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MemberInfoDO
 * 
 * @author sunxp
 */
@Data
@NoArgsConstructor
public class MemberInfoDO implements Serializable {

    private static final long serialVersionUID = -7029495333118659521L;

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

    public MemberInfoDO(String memberId) {
        this.memberId = memberId;
    }

}