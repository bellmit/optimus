package com.optimus.web.order;

import java.util.List;

import com.optimus.manager.order.dto.OrderInfoDTO;
import com.optimus.service.order.OrderService;
import com.optimus.web.order.convert.OrderControllerConvert;
import com.optimus.web.order.req.QueryOrderInfoReq;
import com.optimus.web.order.resp.QueryOrderInfoResp;
import com.optimus.web.order.validate.OrderControllerValidate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单Controller
 * 
 * @author sunxp
 */
@RestController
@RequestMapping(value = "/optimus/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 查询订单信息
     * 
     * @param req
     * @return
     */
    @GetMapping("/queryOrderInfo")
    public QueryOrderInfoResp queryOrderInfo(@RequestBody QueryOrderInfoReq req) {

        OrderControllerValidate.validateQueryOrderInfo(req);

        OrderInfoDTO orderInfo = OrderControllerConvert.getOrderInfoDTO(req);

        // 查询订单信息
        List<OrderInfoDTO> orderInfoList = orderService.listOrderInfoByOrderInfoQuerys(orderInfo, null);

        return OrderControllerConvert.getQueryOrderInfoResp(orderInfoList.get(0));

    }

}
