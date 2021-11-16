package com.optimus.tps.order.impl;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.optimus.tps.order.OrderTps;
import com.optimus.tps.order.req.OrderNoticeReq;
import com.optimus.util.JacksonUtil;
import com.optimus.util.SignUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单Tps实现
 * 
 * @author sunxp
 */
@Component
@Slf4j
public class OrderTpsImpl implements OrderTps {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String orderNotice(OrderNoticeReq req, String key, String noticeUrl) {

        log.info("orderNotice req is {}, noticeUrl is {}", req, noticeUrl);

        try {

            // 加签
            String reqString = JacksonUtil.toString(req);
            Map<String, Object> map = JacksonUtil.toBean(reqString, new TypeReference<Map<String, Object>>() {
            });

            req.setSign(SignUtil.sign(map, key));

            // Post请求
            ResponseEntity<String> resp = restTemplate.postForEntity(noticeUrl, req, String.class);

            return resp.getBody();

        } catch (Exception e) {
            log.error("orderNotice is error", e);
        }

        return null;

    }

}
