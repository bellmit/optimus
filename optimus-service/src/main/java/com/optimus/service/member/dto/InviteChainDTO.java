package com.optimus.service.member.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邀请链DTO
 * 
 * @author sunxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteChainDTO implements Serializable {

    private static final long serialVersionUID = -5860137738423012704L;

    /**
     * 索引
     */
    private Integer index;

    /**
     * 父索引
     */
    private Integer parentIndex;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 邀请链DTO
     */
    private InviteChainDTO inviteChainDTO;

}
