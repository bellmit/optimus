package com.optimus.manager.order.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.optimus.dao.domain.OrderInfoDO;
import com.optimus.dao.mapper.OrderInfoDao;
import com.optimus.manager.order.OrderManager;
import com.optimus.manager.order.dto.OrderNoticeInputDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.JacksonUtil;
import com.optimus.util.SignUtil;
import com.optimus.util.constants.RespCodeEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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

    @Resource
    private OrderInfoDao orderInfoDao;

    @Override
    public void checkCallerOrderId(String callerOrderId) {

        // 查询订单信息
        OrderInfoDO orderInfo = orderInfoDao.getOrderInfoByCallerOrderId(callerOrderId);

        // 验证订单是否存在
        AssertUtil.empty(orderInfo, RespCodeEnum.ORDER_EXIST_ERROR, null);

    }

    @Override
    public String orderNotice(OrderNoticeInputDTO input, String key, String noticeUrl) {

        log.info("orderNotice orderNoticeInput is {}, key is {}, noticeUrl is {}", input, key, noticeUrl);

        try {

            // 加签
            String inputString = JacksonUtil.toString(input);
            Map<String, Object> map = JacksonUtil.toBean(inputString, new TypeReference<Map<String, Object>>() {
            });

            // 设置签名
            input.setSign(SignUtil.sign(map, key));

            // Post
            ResponseEntity<String> resp = restTemplate.postForEntity(noticeUrl, input, String.class);

            return resp.getBody();

        } catch (Exception e) {
            log.error("orderNotice is error", e);
        }

        return null;

    }

}
