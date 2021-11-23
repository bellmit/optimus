package com.optimus.dao.query;

import java.io.Serializable;
import java.util.List;

import com.optimus.util.constants.member.MemberTypeEnum;

import lombok.Data;

/**
 * 会员渠道Query
 * 
 * @author sunxp
 */
@Data
public class MemberChannelQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 代理会员编号
     */
    private String agentMemberId;

    /**
     * 会员类型[冗余一份用以匹配渠道]
     * 
     * @see MemberTypeEnum
     */
    private String memberType;

    /**
     * 网关子渠道编号集合
     */
    private List<String> subChannelCodeList;

}
