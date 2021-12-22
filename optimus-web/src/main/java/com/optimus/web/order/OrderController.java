package com.optimus.web.order;

import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.service.order.OrderService;
import com.optimus.util.annotation.OptimusRateLimiter;
import com.optimus.web.order.convert.OrderControllerConvert;
import com.optimus.web.order.req.QueryOrderInfoReq;
import com.optimus.web.order.resp.QueryOrderInfoResp;
import com.optimus.web.order.validate.OrderControllerValidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单Controller
 * 
 * @author sunxp
 */
@RestController
@RequestMapping(value = "/optimus/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 查询订单信息
     * 
     * @param req
     * @return
     */
    @OptimusRateLimiter(permits = 100D, timeout = 0)
    @GetMapping("/queryOrderInfo")
    public QueryOrderInfoResp queryOrderInfo(@RequestBody QueryOrderInfoReq req) {

        log.info("查询订单信息,请求:{}", req);

        // 验证订单信息
        OrderControllerValidate.validateQueryOrderInfo(req);

        // 查询订单信息
        OrderInfoDTO orderInfo = orderService.getOrderInfoByMemberIdAndCallerOrderId(req.getMemberId(), req.getCallerOrderId());

        QueryOrderInfoResp resp = OrderControllerConvert.getQueryOrderInfoResp(orderInfo);
        log.info("查询订单信息,响应:{}", resp);

        return resp;

    }

}
