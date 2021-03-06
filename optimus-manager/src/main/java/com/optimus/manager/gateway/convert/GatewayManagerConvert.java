package com.optimus.manager.gateway.convert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import com.optimus.dao.domain.GatewaySubChannelDO;
import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.query.GatewaySubChannelQuery;
import com.optimus.manager.gateway.dto.GatewayChannelDTO;
import com.optimus.manager.gateway.dto.GatewaySubChannelDTO;
import com.optimus.manager.gateway.dto.MatchChannelDTO;
import com.optimus.manager.member.dto.MemberInfoDTO;
import com.optimus.util.constants.gateway.GatewayChannelStatusEnum;
import com.optimus.util.constants.gateway.GatewayFaceValueTypeEnum;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 网关ManagerConvert
 * 
 * @author sunxp
 */
public class GatewayManagerConvert {

    /**
     * 获取网关子渠道Query
     * 
     * @param memberInfo
     * @param gatewayChannel
     * @return
     */
    public static GatewaySubChannelQuery getGatewaySubChannelQuery(MemberInfoDTO memberInfo, GatewayChannelDTO gatewayChannel) {

        // 网关子渠道Query
        GatewaySubChannelQuery query = new GatewaySubChannelQuery();

        query.setParentChannelCode(gatewayChannel.getChannelCode());
        query.setChannelStatus(GatewayChannelStatusEnum.CHANNEL_STATUS_Y.getCode());

        return query;

    }

    /**
     * 获取真实的网关子渠道
     * 
     * @param list0
     * @param list1
     * @return
     */
    public static List<GatewaySubChannelDO> getGatewaySubChannelList(List<MemberChannelDO> list0, List<GatewaySubChannelDO> list1) {

        // 真实的网关子渠道
        List<GatewaySubChannelDO> gatewaySubChannelList = new ArrayList<>();

        // 有效的会员渠道
        Set<String> set = new HashSet<>();
        for (MemberChannelDO item : list0) {
            set.add(item.getChannelCode() + item.getSubChannelCode());
        }

        for (GatewaySubChannelDO item : list1) {

            // 不匹配
            if (!set.contains(item.getParentChannelCode() + item.getChannelCode())) {
                continue;
            }

            gatewaySubChannelList.add(item);

        }

        return gatewaySubChannelList;
    }

    /**
     * 获取可用的网关子渠道
     * 
     * @param list
     * @param amount
     * @return
     */
    public static List<GatewaySubChannelDO> getGatewaySubChannelList(List<GatewaySubChannelDO> list, BigDecimal amount) {

        List<GatewaySubChannelDO> gatewaySubChannelList = new ArrayList<>();

        // 面额筛选
        for (GatewaySubChannelDO item : list) {

            boolean flag = false;

            // 面额
            String[] faceValues = item.getFaceValue().split(",");

            // 固定面额
            if (StringUtils.pathEquals(GatewayFaceValueTypeEnum.FACE_VALUE_TYPE_F.getCode(), item.getFaceValueType())) {
                long count = Arrays.asList(faceValues).stream().filter(e -> new BigDecimal(e).compareTo(amount) == 0).count();
                flag = (count > 0) ? true : false;
            }

            // 范围面额
            if (StringUtils.pathEquals(GatewayFaceValueTypeEnum.FACE_VALUE_TYPE_S.getCode(), item.getFaceValueType())) {
                BigDecimal l = new BigDecimal(faceValues[0]);
                BigDecimal r = new BigDecimal(faceValues[1]);
                flag = (l.compareTo(amount) <= 0 && amount.compareTo(r) <= 0) ? true : false;
            }

            // 命中面额加入网关子渠道
            if (flag) {
                gatewaySubChannelList.add(item);
            }

        }

        // 网关子渠道为空
        if (CollectionUtils.isEmpty(gatewaySubChannelList)) {
            return gatewaySubChannelList;
        }

        // 随机加权
        Map<Short, List<GatewaySubChannelDO>> map = gatewaySubChannelList.stream().collect(Collectors.groupingBy(GatewaySubChannelDO::getWeight));
        Iterator<Entry<Short, List<GatewaySubChannelDO>>> iterator = map.entrySet().iterator();

        int random = ThreadLocalRandom.current().nextInt(BigDecimal.TEN.intValue());
        double temp = BigDecimal.ZERO.doubleValue();

        while (iterator.hasNext()) {

            Entry<Short, List<GatewaySubChannelDO>> entry = iterator.next();
            double real = entry.getValue().get(0).getWeight().doubleValue();

            if (random > temp && random <= (temp + real)) {
                gatewaySubChannelList = entry.getValue();
                break;
            }

            temp += real;

        }

        return gatewaySubChannelList;

    }

    /**
     * 获取匹配渠道DTO
     * 
     * @param gatewaySubChannelList
     * @return
     */
    public static MatchChannelDTO getMatchChannelDTO(List<GatewaySubChannelDO> gatewaySubChannelList) {

        // 洗牌
        Collections.shuffle(gatewaySubChannelList);

        // 网关子渠道DTO
        GatewaySubChannelDTO gatewaySubChannel = new GatewaySubChannelDTO();
        BeanUtils.copyProperties(gatewaySubChannelList.get(0), gatewaySubChannel);

        // 匹配渠道DTO
        MatchChannelDTO matchChannel = new MatchChannelDTO();
        matchChannel.setGatewaySubChannel(gatewaySubChannel);

        return matchChannel;

    }

    /**
     * 获取匹配渠道DTO
     * 
     * @param memberInfoList
     * @param memberChannelList
     * @param gatewaySubChannelList
     * @return
     */
    public static MatchChannelDTO getMatchChannelDTO(List<MemberInfoDO> memberInfoList, List<MemberChannelDO> memberChannelList, List<GatewaySubChannelDO> gatewaySubChannelList) {

        // 洗牌
        Collections.shuffle(memberInfoList);
        Collections.shuffle(memberChannelList);

        // 第一个码商
        String codeMemberId = memberInfoList.get(0).getMemberId();

        // 相匹配的会员渠道
        MemberChannelDO memberChannel = memberChannelList.stream().filter(e -> StringUtils.pathEquals(e.getMemberId(), codeMemberId)).findFirst().get();

        // 相匹配的子渠道
        GatewaySubChannelDO gatewaySubChannelDO = gatewaySubChannelList.stream().filter(e -> StringUtils.pathEquals(e.getChannelCode(), memberChannel.getSubChannelCode())).findFirst().get();

        // 网关子渠道DTO
        GatewaySubChannelDTO gatewaySubChannel = new GatewaySubChannelDTO();
        BeanUtils.copyProperties(gatewaySubChannelDO, gatewaySubChannel);

        // 匹配渠道DTO
        MatchChannelDTO matchChannel = new MatchChannelDTO();
        matchChannel.setCodeMemberId(codeMemberId);
        matchChannel.setGatewaySubChannel(gatewaySubChannel);

        return matchChannel;

    }

}
