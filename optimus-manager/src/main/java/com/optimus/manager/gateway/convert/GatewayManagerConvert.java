package com.optimus.manager.gateway.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.optimus.dao.domain.GatewaySubChannelDO;
import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.manager.gateway.dto.GatewaySubChannelDTO;
import com.optimus.manager.gateway.dto.MatchChannelDTO;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.util.constants.gateway.GatewayChannelGroupEnum;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * 网关Manager转换器
 * 
 * @author sunxp
 */
public class GatewayManagerConvert {

    /**
     * 获取会员渠道DTO列表
     * 
     * @param memberInfo
     * @param memberChannelList
     * @return
     */
    public static List<MemberChannelDO> getMemberChannelDTOList(MemberInfoDTO memberInfo, List<MemberChannelDO> memberChannelDOList) {

        Map<String, MemberChannelDO> memberChannelMap = memberChannelDOList.stream().filter(e -> StringUtils.pathEquals(memberInfo.getMemberId(), e.getMemberId())).collect(Collectors.toMap(MemberChannelDO::getSubChannelCode, e -> e));

        List<MemberChannelDO> memberChannelList = new ArrayList<>();

        for (MemberChannelDO item : memberChannelDOList) {

            String memberId = item.getMemberId();
            BigDecimal rate = item.getRate();

            if (StringUtils.pathEquals(memberInfo.getMemberId(), memberId)) {
                continue;
            }

            MemberChannelDO memberChannel = memberChannelMap.get(item.getSubChannelCode());
            if (Objects.isNull(memberChannel) || Objects.isNull(memberChannel.getRate())) {
                continue;
            }

            if (memberChannel.getRate().compareTo(rate) < 0) {
                continue;
            }

            memberChannelList.add(item);

        }

        return memberChannelList;

    }

    /**
     * 获取一个子渠道实例
     * 
     * @param gatewaySubChannelList
     * @return
     */
    public static GatewaySubChannelDO getGatewaySubChannelDO(List<GatewaySubChannelDO> gatewaySubChannelList) {

        Collections.shuffle(gatewaySubChannelList);

        double temp = BigDecimal.ZERO.doubleValue();
        double random = ThreadLocalRandom.current().nextDouble(BigDecimal.TEN.intValue());

        for (GatewaySubChannelDO item : gatewaySubChannelList) {

            double real = item.getWeight().doubleValue();

            if (random > temp && random <= (temp + real)) {
                return item;
            }

            temp += real;

        }

        return null;

    }

    /**
     * 获取匹配渠道DTO
     * 
     * @param memberChannelList
     * @param gatewaySubChannelDO
     * @param channelGroup
     * @return
     */
    public static MatchChannelDTO getMatchChannelDTO(List<MemberChannelDO> memberChannelList, GatewaySubChannelDO gatewaySubChannelDO, String channelGroup) {

        memberChannelList = memberChannelList.stream().filter(e -> StringUtils.pathEquals(gatewaySubChannelDO.getChannelCode(), e.getSubChannelCode())).collect(Collectors.toList());

        int index = ThreadLocalRandom.current().nextInt(memberChannelList.size());
        MemberChannelDO memberChannel = memberChannelList.get(index);

        GatewaySubChannelDTO gatewaySubChannel = new GatewaySubChannelDTO();
        BeanUtils.copyProperties(gatewaySubChannelDO, gatewaySubChannel);

        MatchChannelDTO matchChannel = new MatchChannelDTO();
        matchChannel.setGatewaySubChannel(gatewaySubChannel);

        if (StringUtils.pathEquals(GatewayChannelGroupEnum.GATEWAY_CHANNEL_GROUP_I.getCode(), channelGroup)) {
            matchChannel.setRate(memberChannel.getRate());
        }

        if (StringUtils.pathEquals(GatewayChannelGroupEnum.GATEWAY_CHANNEL_GROUP_O.getCode(), channelGroup)) {
            matchChannel.setCodeMemberId(memberChannel.getMemberId());
        }

        return matchChannel;

    }

}
