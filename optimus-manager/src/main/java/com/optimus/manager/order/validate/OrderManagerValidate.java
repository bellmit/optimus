package com.optimus.manager.order.validate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.result.MemberInfoChainResult;
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

    /** 会员信息链表达式 */
    private static final String MERCHANT_CHAIN_EXP = "^C+A+MS$";

    private static final String MEMBER_TYPE_A = MemberTypeEnum.MEMBER_TYPE_A.getCode();
    private static final String MEMBER_TYPE_B = MemberTypeEnum.MEMBER_TYPE_B.getCode();
    private static final String MEMBER_TYPE_C = MemberTypeEnum.MEMBER_TYPE_C.getCode();

    /**
     * 验证会员信息链
     * 
     * @param chainList
     * @param memberChannelList
     */
    public static void validateMemberChain(List<MemberInfoChainResult> chainList, List<MemberChannelDO> memberChannelList) {

        // 断言:非空
        AssertUtil.notEmpty(chainList, RespCodeEnum.MEMBER_ERROR, "会员信息链为空");
        AssertUtil.notEmpty(memberChannelList, RespCodeEnum.MEMBER_CHANNEL_ERROR, "会员渠道为空");

        // 注意:会员信息链上没有商户会员编号,需加上商户会员编号作为验证依据
        if ((chainList.size() + 1) != memberChannelList.size()) {
            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员信息链和会员渠道不匹配");
        }

        // 验证会员渠道
        validateMerchant(memberChannelList);

        // 验证会员类型
        validateMemberType(chainList);

        // 验证会员信息链费率
        validateRate(chainList, memberChannelList);

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
        long count = memberChannelList.stream().filter(e -> StringUtils.pathEquals(MEMBER_TYPE_B, e.getMemberType())).count();
        if (count == 1) {
            return;
        }

        // 商户有且只能有一个
        throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员信息链中有且只有一个商户");

    }

    /**
     * 验证会员类型
     * 
     * @param chainList
     */
    private static void validateMemberType(List<MemberInfoChainResult> chainList) {

        // 排序:码商[n]->代理[n]->管理[1]->平台[1]
        chainList.sort((l, r) -> Short.compare(l.getLevel(), r.getLevel()));

        // 会员信息链:类型
        String chain = chainList.stream().map(MemberInfoChainResult::getMemberType).collect(Collectors.joining(""));

        // 正则匹配
        Matcher matcher = Pattern.compile(MERCHANT_CHAIN_EXP).matcher(chain);
        if (matcher.matches()) {
            return;
        }

        // 错误的会员关系
        throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员信息链异常");

    }

    /**
     * 验证费率
     * 
     * @param chainList
     * @param memberChannelList
     */
    private static void validateRate(List<MemberInfoChainResult> chainList, List<MemberChannelDO> memberChannelList) {

        // 构建会员费率Map
        Map<String, BigDecimal> map = memberChannelList.stream().collect(Collectors.toMap(MemberChannelDO::getMemberId, MemberChannelDO::getRate));

        // 获取商户的渠道
        MemberChannelDO memberChannel = memberChannelList.stream().filter(e -> StringUtils.pathEquals(MEMBER_TYPE_B, e.getMemberType())).findFirst().get();

        // 排序:码商[n]->代理[n]->管理[1]->平台[1]
        chainList.sort((l, r) -> Short.compare(l.getLevel(), r.getLevel()));

        // 验证会员信息链费率
        chainList.stream().reduce((l, r) -> {

            // 类型
            String lMemberType = l.getMemberType();
            String rMemberType = r.getMemberType();

            // 费率
            BigDecimal lRate = map.get(l.getMemberId());
            BigDecimal rRate = map.get(r.getMemberId());

            // 码商->码商:费率依次递减
            if (StringUtils.pathEquals(MEMBER_TYPE_C, rMemberType) && lRate.compareTo(rRate) <= 0) {
                return r;
            }

            // 代理->管理->平台:费率依次递增
            if (!StringUtils.pathEquals(MEMBER_TYPE_C, lMemberType) && lRate.compareTo(rRate) >= 0) {
                return r;
            }

            // 代理-商户-码商:费率商户>=码商
            if (StringUtils.pathEquals(MEMBER_TYPE_C, lMemberType) && StringUtils.pathEquals(MEMBER_TYPE_A, rMemberType) && memberChannel.getRate().compareTo(lRate) >= 0) {
                return r;
            }

            throw new OptimusException(RespCodeEnum.MEMBER_ERROR, "会员信息链费率配置错误");

        });

    }

}
