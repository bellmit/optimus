package com.optimus.service.member.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * InviteChainModel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InviteChainModel implements Serializable {

    private static final long serialVersionUID = -5860137738423012704L;

    private int index;

    private int parentIndex;

    private String memberId;

    private InviteChainModel inviteChainModel;

}
