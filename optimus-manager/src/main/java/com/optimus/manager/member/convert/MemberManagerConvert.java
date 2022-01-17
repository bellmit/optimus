package com.optimus.manager.member.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.query.MemberChannelQuery;
import com.optimus.dao.query.MemberInfoQuery;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.util.constants.member.MemberDeleteFlagEnum;
import com.optimus.util.constants.member.MemberStatusEnum;
import com.optimus.util.constants.member.MemberTypeEnum;

/**
 * 会员ManagerConvert
 * 
 * @author sunxp
 */
public class MemberManagerConvert {

    /**
     * 获取会员信息Query
     * 
     * @param memberChannelList
     * @return
     */
    public static MemberInfoQuery getMemberInfoQuery(List<MemberChannelDO> memberChannelList) {

        // 会员信息Query
        MemberInfoQuery query = new MemberInfoQuery();
        query.setMemberIdList(memberChannelList.stream().map(MemberChannelDO::getMemberId).collect(Collectors.toList()));
        query.setMemberStatus(MemberStatusEnum.MEMBER_STATUS_Y.getCode());
        query.setDeleteFlag(MemberDeleteFlagEnum.DELETE_FLAG_ND.getCode());

        return query;

    }

    /**
     * 获取会员渠道Query
     * 
     * @param memberInfo
     * @param subChannelCodeList
     * @return
     */
    public static MemberChannelQuery getMemberChannelQuery(MemberInfoDTO memberInfo, List<String> subChannelCodeList) {

        // 会员渠道Query
        MemberChannelQuery query = new MemberChannelQuery();

        query.setAgentMemberId(memberInfo.getSupDirectMemberId());
        query.setMemberType(MemberTypeEnum.MEMBER_TYPE_C.getCode());
        query.setSubChannelCodeList(subChannelCodeList);

        return query;
    }

    /**
     * 获取会员渠道Query
     * 
     * @param channelCode
     * @param subChannelCode
     * @param memberIdList
     * @return
     */
    public static MemberChannelQuery getMemberChannelQuery(String channelCode, String subChannelCode, List<String> memberIdList) {

        // 会员渠道Query
        MemberChannelQuery query = new MemberChannelQuery();

        query.setChannelCode(channelCode);
        query.setSubChannelCode(subChannelCode);
        query.setMemberIdList(memberIdList);

        return query;
    }

    /**
     * 获取真实的会员渠道
     * 
     * @param list0
     * @param list1
     * @return
     */
    public static List<MemberChannelDO> getMemberChannelList(List<MemberInfoDO> list0, List<MemberChannelDO> list1) {

        // 真实的会员渠道
        List<MemberChannelDO> memberChannelList = new ArrayList<>();

        // 有效的会员编号
        List<String> memberIdList = list0.stream().map(MemberInfoDO::getMemberId).distinct().collect(Collectors.toList());

        for (MemberChannelDO item : list1) {

            // 不匹配
            if (!memberIdList.contains(item.getMemberId())) {
                continue;
            }

            memberChannelList.add(item);

        }

        return memberChannelList;
    }

}
