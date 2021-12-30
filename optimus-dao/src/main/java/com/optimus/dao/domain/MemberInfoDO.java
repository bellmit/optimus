package com.optimus.dao.domain;

import java.io.Serializable;
import java.util.Date;

import com.optimus.util.constants.member.MemberDeleteFlagEnum;
import com.optimus.util.constants.member.MemberStatusEnum;
import com.optimus.util.constants.member.MemberTypeEnum;

import lombok.Data;

/**
 * 会员信息DO
 * 
 * @author sunxp
 */
@Data
public class MemberInfoDO implements Serializable {

    private static final long serialVersionUID = -7029495333118659521L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 会员密钥
     */
    private String memberKey;

    /**
     * 会员类型
     * 
     * @see MemberTypeEnum
     */
    private String memberType;

    /**
     * 会员状态
     * 
     * @see MemberStatusEnum
     */
    private String memberStatus;

    /**
     * 直接上级会员编号
     */
    private String supDirectMemberId;

    /**
     * 组织编号
     */
    private Long organizeId;

    /**
     * 删除标识
     * 
     * @see MemberDeleteFlagEnum
     */
    private String deleteFlag;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;

}