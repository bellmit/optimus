package com.optimus.service.member.dto;

import java.io.Serializable;

import com.optimus.util.constants.MemberEnum;
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
     * @see MemberEnum
     */
    private String memberType;

    /**
     * 会员状态
     */
    private String memberStatus;

    /**
     * 直接上级会员编号
     */
    private String supDirectMemberId;

    public MemberInfoDTO(String memberId) {
        this.memberId = memberId;
    }

}