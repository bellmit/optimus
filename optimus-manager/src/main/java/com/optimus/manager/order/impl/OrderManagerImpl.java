package com.optimus.manager.order.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.MemberInfoDao;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.OrderNoticeInputDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.DateUtil;
import com.optimus.util.JacksonUtil;
import com.optimus.util.SignUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.order.OrderReleaseStatusEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单managerImpl
 *
 * @author hongp
 */
@Component
@Slf4j
public class OrderManagerImpl implements OrderManager {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccountManager accountManager;

    @Resource
    private MemberInfoDao memberInfoDao;

    @Resource
    private OrderInfoDao orderInfoDao;

    @Override
    public void checkCallerOrderId(String callerOrderId) {

        // 查询订单信息
        OrderInfoDO orderInfo = orderInfoDao.getOrderInfoByCallerOrderId(callerOrderId);

        // 验证订单是否存在
        AssertUtil.empty(orderInfo, RespCodeEnum.ORDER_EXIST_ERROR, null);

    }

    @Async
    @Override
    public void asyncRelease(OrderInfoDTO orderInfo) {

        // 无需释放的订单
        if (StringUtils.pathEquals(OrderReleaseStatusEnum.RELEASE_STATUS_D.getCode(), orderInfo.getReleaseStatus())) {
            return;
        }

        // 更新释放状态
        int update = orderInfoDao.updateOrderInfoByOrderIdAndReleaseStatus(orderInfo.getOrderId(), OrderReleaseStatusEnum.RELEASE_STATUS_Y.getCode(), OrderReleaseStatusEnum.RELEASE_STATUS_N.getCode(), DateUtil.currentDate());
        if (update != 1) {
            return;
        }

        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_PLUS, orderInfo, "渠道回调加余额户"));
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.F_MINUS, orderInfo, "渠道回调减冻结户"));

        boolean doTrans = accountManager.doTrans(doTransList);
        if (doTrans) {
            return;
        }

        // 记账失败,回滚释放状态
        orderInfoDao.updateOrderInfoByOrderIdAndReleaseStatus(orderInfo.getOrderId(), OrderReleaseStatusEnum.RELEASE_STATUS_N.getCode(), OrderReleaseStatusEnum.RELEASE_STATUS_Y.getCode(), DateUtil.currentDate());

    }

    @Async
    @Override
    public void asyncSplitProfit(String orderId, List<MemberInfoChainResult> chainList, List<MemberChannelDO> memberChannelList) {
        // TODO Auto-generated method stub

    }

    @Async
    @Override
    public void asyncOrderNotice(OrderNoticeInputDTO input, String noticeUrl) {

        log.info("asyncOrderNotice orderNoticeInput is {}, noticeUrl is {}", input, noticeUrl);

        try {

            // 查询会员信息
            MemberInfoDO memberInfo = memberInfoDao.getMemberInfoByMemberId(input.getMemberId());

            // 加签
            String inputString = JacksonUtil.toString(input);
            Map<String, Object> map = JacksonUtil.toBean(inputString, new TypeReference<Map<String, Object>>() {
            });

            // 设置签名
            input.setSign(SignUtil.sign(map, memberInfo.getMemberKey()));

            // Post
            restTemplate.postForEntity(noticeUrl, input, String.class);

        } catch (Exception e) {
            log.error("asyncOrderNotice is error", e);
        }

    }

}
