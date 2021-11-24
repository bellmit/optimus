package com.optimus.dao.result;

import java.io.Serializable;

import com.optimus.util.constants.member.MemberTypeEnum;

import lombok.Data;

/**
 * 递归查询会员信息Result
 * 
 * @author sunxp
 */
@Data
public class MemberInfoForRecursionResult implements Serializable {

    private static final long serialVersionUID = -8753332987533130915L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 会员类型
     * 
     * @see MemberTypeEnum
     */
    private String memberType;

    /**
     * 层级[越大层级越高]
     */
    private Short level;

}
