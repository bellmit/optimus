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
import com.optimus.manager.order.dto.OrderNoticeDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.DateUtil;
import com.optimus.util.JacksonUtil;
import com.optimus.util.SignUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.order.OrderMerchantNotifyStatusEnum;
import com.optimus.util.constants.order.OrderReleaseStatusEnum;
import com.optimus.util.constants.order.OrderSplitProfitStatusEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Long idempotent(OrderInfoDTO orderInfo) {

        // 查询订单信息
        OrderInfoDO orderInfoDO = orderInfoDao.getOrderInfoByCallerOrderId(orderInfo.getCallerOrderId());
        AssertUtil.empty(orderInfoDO, RespCodeEnum.ORDER_EXIST_ERROR, null);

        try {

            // 获取订单信息DO
            orderInfoDO = OrderManagerConvert.getOrderInfoDO(orderInfo);

            // 新增订单信息
            orderInfoDao.addOrderInfo(orderInfoDO);

        } catch (Exception e) {
            log.error("订单幂等异常:", e);
            return null;
        }

        // 返回订单信息主键
        return orderInfoDO.getId();

    }

    @Override
    public boolean release(OrderInfoDTO orderInfo) {

        // 无需释放的订单
        if (StringUtils.pathEquals(OrderReleaseStatusEnum.RELEASE_STATUS_D.getCode(), orderInfo.getReleaseStatus())) {
            return false;
        }

        // 更新释放状态
        int update = orderInfoDao.updateOrderInfoByOrderIdAndReleaseStatus(orderInfo.getOrderId(), OrderReleaseStatusEnum.RELEASE_STATUS_Y.getCode(), OrderReleaseStatusEnum.RELEASE_STATUS_N.getCode(), DateUtil.currentDate());
        if (update != 1) {
            return false;
        }

        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.B_PLUS, orderInfo, "释放加余额户"));
        doTransList.add(OrderManagerConvert.getDoTransDTO(AccountChangeTypeEnum.F_MINUS, orderInfo, "释放减冻结户"));

        boolean doTrans = accountManager.doTrans(doTransList);
        if (!doTrans) {
            // 记账失败,回滚释放状态
            orderInfoDao.updateOrderInfoByOrderIdAndReleaseStatus(orderInfo.getOrderId(), OrderReleaseStatusEnum.RELEASE_STATUS_N.getCode(), OrderReleaseStatusEnum.RELEASE_STATUS_Y.getCode(), DateUtil.currentDate());
        }

        return doTrans;

    }

    @Override
    public boolean splitProfit(OrderInfoDTO orderInfo, List<MemberInfoChainResult> chainList, List<MemberChannelDO> memberChannelList) {

        // 账户交易List
        List<DoTransDTO> doTransList = OrderManagerConvert.getDoTransDTOList(orderInfo, chainList, memberChannelList);

        // 更新订单分润状态
        int update = orderInfoDao.updateOrderInfoByOrderIdAndSplitProfitStatus(orderInfo.getOrderId(), OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_Y.getCode(), OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_N.getCode(), DateUtil.currentDate());
        if (update != 1) {
            return false;
        }

        // 记账
        boolean doTrans = accountManager.doTrans(doTransList);
        if (!doTrans) {
            // 记账失败,回滚分润状态
            orderInfoDao.updateOrderInfoByOrderIdAndSplitProfitStatus(orderInfo.getOrderId(), OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_N.getCode(), OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_Y.getCode(), DateUtil.currentDate());
        }

        return doTrans;

    }

    @Override
    public boolean orderNotice(OrderInfoDTO orderInfo) {

        // 获取订单通知DTO
        OrderNoticeDTO orderNotice = OrderManagerConvert.getOrderNoticeDTO(orderInfo);

        // 查询会员信息
        MemberInfoDO memberInfo = memberInfoDao.getMemberInfoByMemberId(orderNotice.getMemberId());

        // 加签
        Map<String, Object> map = JacksonUtil.toBean(JacksonUtil.toString(orderNotice), new TypeReference<Map<String, Object>>() {
        });

        // 设置签名
        orderNotice.setSign(SignUtil.sign(map, memberInfo.getMemberKey()));

        // Post
        ResponseEntity<String> entity = restTemplate.postForEntity(orderInfo.getMerchantCallbackUrl(), orderNotice, String.class);

        // 下游响应不成功
        if (!StringUtils.pathEquals(HttpStatus.OK.name(), entity.getBody())) {
            return false;
        }

        // 更新订单通知状态
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        orderInfoDO.setId(orderInfo.getId());
        orderInfoDO.setMerchantNotifyStatus(OrderMerchantNotifyStatusEnum.MERCHANT_NOTIFY_STATUS_NS.getCode());
        orderInfoDO.setUpdateTime(DateUtil.currentDate());
        orderInfoDao.updateOrderInfo(orderInfoDO);

        return true;

    }

    @Async
    @Override
    public void asyncRelease(OrderInfoDTO orderInfo) {

        try {

            release(orderInfo);

        } catch (OptimusException e) {
            log.error("异步释放订单异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
        } catch (Exception e) {
            log.error("异步释放订单异常:", e);
        }
    }

    @Async
    @Override
    public void asyncSplitProfit(OrderInfoDTO orderInfo, List<MemberInfoChainResult> chainList, List<MemberChannelDO> memberChannelList) {

        try {

            splitProfit(orderInfo, chainList, memberChannelList);

        } catch (OptimusException e) {
            log.error("异步订单分润异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
        } catch (Exception e) {
            log.error("异步订单分润异常:", e);
        }
    }

    @Async
    @Override
    public void asyncOrderNotice(OrderInfoDTO orderInfo) {

        try {

            orderNotice(orderInfo);

        } catch (OptimusException e) {
            log.error("异步订单通知异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
        } catch (Exception e) {
            log.error("异步订单通知异常:", e);
        }

    }

}
