package com.optimus.manager.member.dto;

import java.io.Serializable;

import com.optimus.util.constants.member.MemberDeleteFlagEnum;
import com.optimus.util.constants.member.MemberStatusEnum;
import com.optimus.util.constants.member.MemberTypeEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员信息DTO
 * 
 * @author sunxp
 */
@Data
@NoArgsConstructor
public class MemberInfoDTO implements Serializable {

    private static final long serialVersionUID = -2479933606115676567L;

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

    public MemberInfoDTO(String memberId) {
        this.memberId = memberId;
    }

}