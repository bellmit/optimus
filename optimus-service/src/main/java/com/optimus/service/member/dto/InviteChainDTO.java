package com.optimus.service.member.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InviteChainDTO
 * 
 * @author sunxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteChainDTO implements Serializable {

    private static final long serialVersionUID = -5860137738423012704L;

    private Integer index;

    private Integer parentIndex;

    private String memberId;

    private InviteChainDTO inviteChainDTO;

}
