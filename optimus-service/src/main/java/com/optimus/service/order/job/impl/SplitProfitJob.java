package com.optimus.service.order.job.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.optimus.dao.domain.MemberChannelDO;
import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.MemberChannelDao;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.dao.query.OrderInfoQuery;
import com.optimus.dao.result.MemberInfoChainResult;
import com.optimus.manager.member.MemberManager;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.manager.order.validate.OrderManagerValidate;
import com.optimus.service.order.job.BaseOrderJob;
import com.optimus.util.AssertUtil;
import com.optimus.util.DateUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.order.OrderSplitProfitStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.exception.OptimusException;
import com.optimus.util.model.page.Page;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单分润
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class SplitProfitJob extends BaseOrderJob {

    /** 订单分润定时任务分片配置项 */
    private static final String SPLIT_PROFIT_JOB_SHARDING_BASE_KEY = "SPLIT_PROFIT_JOB_SHARDING";

    /** 订单分润一次执行上限配置项 */
    private static final String SPLIT_PROFIT_ONCE_EXECUTE_LIMIT_BASE_KEY = "SPLIT_PROFIT_ONCE_EXECUTE_LIMIT";

    /** 订单分润间隔配置项 */
    private static final String SPLIT_PROFIT_INTERVAL_BASE_KEY = "SPLIT_PROFIT_INTERVAL";

    /** 订单分润一次执行上限默认值 */
    private static Integer splitProfitOnceExecuteLimit = 100;

    /** 订单分润间隔默认值 */
    private static Integer splitProfitInterval = 10;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private OrderManager orderManager;

    @Resource
    private MemberChannelDao memberChannelDao;

    @Resource
    private OrderInfoDao orderInfoDao;

    @Scheduled(initialDelay = 60000, fixedDelay = 10000)
    @Override
    public void execute() {

        // 初始化
        init();

        // 订单信息Query
        OrderInfoQuery query = getOrderInfoQuery();
        if (Objects.isNull(query)) {
            return;
        }

        // 下标
        Integer index = 0;

        while (index.compareTo(splitProfitOnceExecuteLimit) < 0) {

            index++;

            // 查询订单
            List<OrderInfoDO> orderInfoList = orderInfoDao.listOrderInfoByOrderInfoQuerys(query);
            if (CollectionUtils.isEmpty(orderInfoList)) {
                break;
            }

            for (OrderInfoDO item : orderInfoList) {

                // 订单信息
                OrderInfoDTO orderInfo = new OrderInfoDTO();
                BeanUtils.copyProperties(item, orderInfo);

                try {

                    // 查询会员信息链
                    List<MemberInfoChainResult> chainList = memberManager.listMemberInfoChains(orderInfo.getCodeMemberId());
                    AssertUtil.notEmpty(chainList, RespCodeEnum.MEMBER_ERROR, "会员信息链不能为空");

                    // 查询会员关系链的会员渠道费率
                    List<String> memberIdList = chainList.stream().map(MemberInfoChainResult::getMemberId).collect(Collectors.toList());
                    memberIdList.add(orderInfo.getMemberId());
                    List<MemberChannelDO> memberChannelList = memberChannelDao.listMemberChannelByMemberIdLists(memberIdList);

                    // 验证链及渠道
                    OrderManagerValidate.validateChainAndChannel(chainList, memberChannelList, orderInfo.getCodeMemberId());

                    // 订单分润
                    orderManager.splitProfit(orderInfo, chainList, memberChannelList);

                } catch (OptimusException e) {
                    log.error("订单分润异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
                } catch (Exception e) {
                    log.error("订单分润异常:", e);
                }

            }

        }

    }

    /**
     * 初始化
     */
    private void init() {

        // 一次执行上限
        String value1 = super.loadSystemConfig(SPLIT_PROFIT_ONCE_EXECUTE_LIMIT_BASE_KEY);
        if (StringUtils.hasLength(value1)) {
            splitProfitOnceExecuteLimit = Integer.parseInt(value1);
        }

        // 间隔
        String value2 = super.loadSystemConfig(SPLIT_PROFIT_INTERVAL_BASE_KEY);
        if (StringUtils.hasLength(value2)) {
            splitProfitInterval = Integer.parseInt(value2);
        }

    }

    /**
     * 获取订单信息Query
     * 
     * @return
     */
    private OrderInfoQuery getOrderInfoQuery() {

        // 分片
        Map<Integer, Integer> shardingMap = super.sharding(SPLIT_PROFIT_JOB_SHARDING_BASE_KEY);
        if (CollectionUtils.isEmpty(shardingMap)) {
            return null;
        }

        // 订单信息Query
        OrderInfoQuery query = new OrderInfoQuery();
        query.setPage(new Page(1, 1000));
        query.setShard(shardingMap.entrySet().stream().findFirst().get().getKey());
        query.setTotalShard(shardingMap.entrySet().stream().findFirst().get().getValue());
        query.setLastTime(DateUtil.offsetForMinute(DateUtil.currentDate(), -splitProfitInterval));
        query.setOrderType(OrderTypeEnum.ORDER_TYPE_C.getCode());
        query.setOrderStatus(OrderStatusEnum.ORDER_STATUS_AP.getCode());
        query.setSplitProfitStatus(OrderSplitProfitStatusEnum.SPLIT_PROFIT_STATUS_N.getCode());

        return query;

    }

}
