package com.optimus.manager.order.validate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.result.MemberInfoForRecursionResult;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.member.MemberTypeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.util.StringUtils;

/**
 * 订单ManagerValidate
 * 
 * @author sunxp
 */
public class OrderManagerValidate {

    /** 会员关系链表达式 */
    private static final String MERCHANT_CHAIN_EXP = "^C+A+MS$";

    /**
     * 验证会员关系链
     * 
     * @param memberInfoList
     * @param memberChannelList
     */
    public static void validateMemberChain(List<MemberInfoForRecursionResult> memberInfoList, List<MemberChannelDO> memberChannelList) {

        // 断言:非空
        AssertUtil.notEmpty(memberInfoList, RespCodeEnum.MEMBER_ERROR, "会员关系链为空");
        AssertUtil.notEmpty(memberChannelList, RespCodeEnum.MEMBER_CHANNEL_ERROR, "会员渠道为空");

        // 注意:会员关系链上没有商户会员编号,需加上商户会员编号作为验证依据
        if ((memberInfoList.size() + 1) != memberChannelList.size()) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员关系链和会员渠道不匹配");
        }

        // 验证会员渠道
        validateMerchant(memberChannelList);

        // 验证会员类型
        validateMemberType(memberInfoList);

        // 验证会员关系链费率
        validateRate(memberInfoList, memberChannelList);

    }

    /**
     * 验证会员渠道
     * 
     * @param memberChannelList
     */
    private static void validateMerchant(List<MemberChannelDO> memberChannelList) {

        // 断言:会员渠道费率
        memberChannelList.stream().forEach(e -> {
            AssertUtil.notEmpty(e.getRate(), RespCodeEnum.MEMBER_CHANNEL_ERROR, "会员渠道费率为空");
        });

        // 获取会员类型为商户的会员
        long count = memberChannelList.stream().filter(e -> StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_B.getCode(), e.getMemberType())).count();
        if (count == 1) {
            return;
        }

        // 商户有且只能有一个
        throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员关系链中有且只有一个商户");

    }

    /**
     * 验证会员类型
     * 
     * @param memberInfoList
     */
    private static void validateMemberType(List<MemberInfoForRecursionResult> memberInfoList) {

        // 排序:码商[n]->代理[n]->管理[1]->平台[1]
        memberInfoList.sort((l, r) -> Short.compare(l.getLevel(), r.getLevel()));

        // 会员类型链
        String chain = memberInfoList.stream().map(MemberInfoForRecursionResult::getMemberType).collect(Collectors.joining(""));

        // 正则匹配
        Matcher matcher = Pattern.compile(MERCHANT_CHAIN_EXP).matcher(chain);
        if (matcher.matches()) {
            return;
        }

        // 错误的会员关系
        throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员关系链异常");

    }

    /**
     * 验证费率
     * 
     * @param memberInfoList
     * @param memberChannelList
     */
    private static void validateRate(List<MemberInfoForRecursionResult> memberInfoList, List<MemberChannelDO> memberChannelList) {

        // 构建会员费率Map
        Map<String, BigDecimal> map = memberChannelList.stream().collect(Collectors.toMap(MemberChannelDO::getMemberId, MemberChannelDO::getRate));

        // 获取商户的渠道
        MemberChannelDO memberChannel = memberChannelList.stream().filter(e -> StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_B.getCode(), e.getMemberType())).findFirst().get();

        // 排序:码商[n]->代理[n]->管理[1]->平台[1]
        memberInfoList.sort((l, r) -> Short.compare(l.getLevel(), r.getLevel()));

        // 验证会员关系链费率
        memberInfoList.stream().reduce((l, r) -> {

            String lMemberType = l.getMemberType();
            String rMemberType = r.getMemberType();

            // 获取费率
            BigDecimal lRate = map.get(l.getMemberId());
            BigDecimal rRate = map.get(r.getMemberId());

            // 上级为码商:费率依次递增
            if (StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), lMemberType) && lRate.compareTo(rRate) >= 0) {
                return null;
            }

            // 下级非码商:费率依次递减
            if (!StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), rMemberType) && lRate.compareTo(rRate) <= 0) {
                return null;
            }

            // 代理-商户-码商费率验证:费率商户>=码商
            if (StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_A.getCode(), lMemberType) && StringUtils.pathEquals(MemberTypeEnum.MEMBER_TYPE_C.getCode(), rMemberType) && memberChannel.getRate().compareTo(rRate) >= 0) {
                return null;
            }

            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员关系链费率配置错误");

        });

    }

}
