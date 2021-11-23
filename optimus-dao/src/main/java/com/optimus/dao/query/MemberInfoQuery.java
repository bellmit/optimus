package com.optimus.dao.query;

import java.io.Serializable;
import java.util.List;

import com.optimus.util.constants.member.MemberDeleteFlagEnum;
import com.optimus.util.constants.member.MemberStatusEnum;

import lombok.Data;

/**
 * 会员信息Query
 * 
 * @author sunxp
 */
@Data
public class MemberInfoQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员编号
     */
    private List<String> memberIdList;

    /**
     * 会员状态
     * 
     * @see MemberStatusEnum
     */
    private String memberStatus;

    /**
     * 删除标识
     * 
     * @see MemberDeleteFlagEnum
     */
    private String deleteFlag;

}
