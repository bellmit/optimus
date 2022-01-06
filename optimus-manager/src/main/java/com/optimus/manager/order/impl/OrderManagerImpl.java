package com.optimus.manager.order.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.optimus.dao.domain.CommonSystemConfigDO;
import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.domain.MemberInfoDO;
import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.CommonSystemConfigDao;
import com.optimus.dao.mapper.MemberChannelDao;
import com.optimus.dao.mapper.MemberInfoDao;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.account.AccountManager;
import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.convert.OrderManagerConvert;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.dto.OrderNoticeDTO;
import com.optimus.manager.order.validate.OrderManagerValidate;
import com.optimus.util.AssertUtil;
import com.optimus.util.DateUtil;
import com.optimus.util.JacksonUtil;
import com.optimus.util.SignUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.common.CommonSystemConfigBaseKeyEnum;
import com.optimus.util.constants.common.CommonSystemConfigTypeEnum;
import com.optimus.util.constants.order.OrderMerchantNotifyStatusEnum;
import com.optimus.util.constants.order.OrderReleaseStatusEnum;
import com.optimus.util.constants.order.OrderSplitProfitStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.model.exception.OptimusException;

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
    private MemberManager memberManager;

    @Autowired
    private CommonSystemConfigDao commonSystemConfigDao;

    @Resource
    private MemberInfoDao memberInfoDao;

    @Resource
    private MemberChannelDao memberChannelDao;

    @Resource
    private OrderInfoDao orderInfoDao;

    @Override
    public void updateOrderInfoToFail(Long id) {

        log.info("更新订单信息为失败,主键:{}", id);

        // 订单信息
        OrderInfoDO orderInfo = new OrderInfoDO();
        orderInfo.setId(id);
        orderInfo.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AF.getCode());
        orderInfo.setUpdateTime(DateUtil.currentDate());

        // 更新订单信息
        orderInfoDao.updateOrderInfo(orderInfo);

    }

    @Override
    public Long idempotent(OrderInfoDTO orderInfo) {

        log.info("订单幂等,订单信息:{}", orderInfo);

        // 查询订单信息
        OrderInfoDO orderInfoDO = orderInfoDao.getOrderInfoByCallerOrderId(orderInfo.getCallerOrderId());
        AssertUtil.empty(orderInfoDO, RespCodeEnum.ORDER_ERROR, "订单已存在");

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

        log.info("订单释放,订单信息:{}", orderInfo);

        // 无需释放的订单
        if (StringUtils.pathEquals(OrderReleaseStatusEnum.RELEASE_STATUS_D.getCode(), orderInfo.getReleaseStatus())) {
            return false;
        }

        // 更新释放状态
        int update = orderInfoDao.updateOrderInfoByOrderIdAndReleaseStatus(orderInfo.getOrderId(), OrderReleaseStatusEnum.RELEASE_STATUS_Y.getCode(), OrderReleaseStatusEnum.RELEASE_STATUS_N.getCode(), DateUtil.currentDate());
        if (update != 1) {
            log.warn("订单释放更新释放状态结果:{}", update);
            return false;
        }

        // 记账
        List<DoTransDTO> doTransList = new ArrayList<>();
        doTransList.add(OrderManagerConvert.getDoTransForCode(AccountChangeTypeEnum.B_PLUS, orderInfo, "释放加余额户"));
        doTransList.add(OrderManagerConvert.getDoTransForCode(AccountChangeTypeEnum.F_MINUS, orderInfo, "释放减冻结户"));

        boolean doTrans = accountManager.doTrans(doTransList);
        if (!doTrans) {
            // 记账失败,回滚释放状态
            orderInfoDao.updateOrderInfoByOrderIdAndReleaseStatus(orderInfo.getOrderId(), OrderReleaseStatusEnum.RELEASE_STATUS_N.getCode(), OrderReleaseStatusEnum.RELEASE_STATUS_Y.getCode(), DateUtil.currentDate());
        }

        log.info("订单释放结果:{}", doTrans);

        return doTrans;

    }

    @Override
    public boolean splitProfit(OrderInfoDTO orderInfo) {

        log.info("订单信息分润,订单信息:{}", orderInfo);

        // 订单状态不为成功
        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AP.getCode(), orderInfo.getOrderStatus())) {
            return false;
        }

        // 查询会员信息链
        List<MemberInfoChainResult> chainList = memberManager.listMemberInfoChains(orderInfo.getCodeMemberId());
        AssertUtil.notEmpty(chainList, RespCodeEnum.MEMBER_ERROR, "会员信息链不能为空");
        log.info("会员信息链:{}", chainList);

        // 查询会员关系链的会员渠道费率
        List<String> memberIdList = chainList.stream().map(MemberInfoChainResult::getMemberId).collect(Collectors.toList());
        memberIdList.add(orderInfo.getMemberId());
        List<MemberChannelDO> memberChannelList = memberChannelDao.listMemberChannelByMemberIdLists(memberIdList);
        OrderManagerValidate.validateChainAndChannel(chainList, memberChannelList, orderInfo.getCodeMemberId());
        log.info("会员渠道费率:{}", memberChannelList);

        // 账户交易List
        List<DoTransDTO> doTransList = OrderManagerConvert.getDoTransDTOList(orderInfo, chainList, memberChannelList);

        // 更新订单分润状态
        int update = orderInfoDao.updateOrderInfoByOrderIdAndSplitProfitStatus(orderInfo.getOrderId(), OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_Y.getCode(), OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_N.getCode(), DateUtil.currentDate());
        if (update != 1) {
            log.warn("订单信息分润更新分润状态结果:{}", update);
            return false;
        }

        // 记账
        boolean doTrans = accountManager.doTrans(doTransList);
        if (!doTrans) {
            // 记账失败,回滚分润状态
            orderInfoDao.updateOrderInfoByOrderIdAndSplitProfitStatus(orderInfo.getOrderId(), OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_N.getCode(), OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_Y.getCode(), DateUtil.currentDate());
        }

        log.info("订单信息分润结果:{}", doTrans);
        return doTrans;

    }

    @Override
    public boolean orderNotice(OrderInfoDTO orderInfo) {

        log.info("订单信息通知,订单信息:{}", orderInfo);

        // 订单状态不为成功或失败
        if (!StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AP.getCode(), orderInfo.getOrderStatus())
                && !StringUtils.pathEquals(OrderStatusEnum.ORDER_STATUS_AF.getCode(), orderInfo.getOrderStatus())) {
            return false;
        }

        // 获取系统配置
        CommonSystemConfigDO commonSystemConfig = commonSystemConfigDao.getCommonSystemConfigByTypeAndBaseKey(CommonSystemConfigTypeEnum.TYPE_BB.getCode(), CommonSystemConfigBaseKeyEnum.BASE_NOTICE_URL.getCode());
        AssertUtil.notEmpty(commonSystemConfig, RespCodeEnum.ERROR_CONFIG, "订单信息通知,未配置系统配置");
        AssertUtil.notEmpty(commonSystemConfig.getValue(), RespCodeEnum.ERROR_CONFIG, "未配置通知地址");

        // 获取订单通知DTO
        OrderNoticeDTO orderNotice = OrderManagerConvert.getOrderNoticeDTO(orderInfo);

        // 查询会员信息
        MemberInfoDO memberInfo = memberInfoDao.getMemberInfoByMemberId(orderNotice.getMemberId());

        // 加签
        Map<String, Object> map = JacksonUtil.toBean(JacksonUtil.toString(orderNotice), new TypeReference<Map<String, Object>>() {
        });

        // 设置签名
        orderNotice.setSign(SignUtil.sign(map, memberInfo.getMemberKey()));
        log.info("订单信息通知报文:{}", orderNotice);

        // Post
        ResponseEntity<String> entity = restTemplate.postForEntity(String.format(commonSystemConfig.getValue(), orderInfo.getMerchantCallbackUrl()), orderNotice, String.class);
        log.info("订单信息通知结果:{}", Objects.isNull(entity) ? entity : entity.getBody());

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
            log.warn("异步释放订单异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
            return;
        } catch (Exception e) {
            log.error("异步释放订单异常:", e);
            return;
        }
    }

    @Async
    @Override
    public void asyncSplitProfit(OrderInfoDTO orderInfo) {

        try {

            splitProfit(orderInfo);

        } catch (OptimusException e) {
            log.warn("异步订单分润异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
            return;
        } catch (Exception e) {
            log.error("异步订单分润异常:", e);
            return;
        }
    }

    @Async
    @Override
    public void asyncOrderNotice(OrderInfoDTO orderInfo) {

        try {

            orderNotice(orderInfo);

        } catch (OptimusException e) {
            log.warn("异步订单通知异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
            return;
        } catch (Exception e) {
            log.error("异步订单通知异常:", e);
            return;
        }

    }

}
