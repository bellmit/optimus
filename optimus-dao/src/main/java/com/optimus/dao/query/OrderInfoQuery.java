package com.optimus.dao.query;

import java.io.Serializable;
import java.util.Date;

import com.optimus.util.constants.order.OrderMerchantNotifyStatusEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.model.page.Page;

import lombok.Data;

/**
 * 订单信息Query
 * 
 * @author sunxp
 */
@Data
public class OrderInfoQuery implements Serializable {

    private static final long serialVersionUID = -9075042761874255007L;

    /**
     * 分片号
     */
    private Integer shard;

    /**
     * 分片总数
     */
    private Integer totalShard;

    /**
     * 最后时间
     */
    private Date lastTime;

    /**
     * 订单类型
     * 
     * @see OrderTypeEnum
     */
    private String orderType;

    /**
     * 订单状态
     * 
     * @see OrderStatusEnum
     */
    private String orderStatus;

    /**
     * 商户通知状态
     * 
     * @see OrderMerchantNotifyStatusEnum
     */
    private String merchantNotifyStatus;

    /**
     * 商户回调次数
     */
    private Short merchantCallbackCount;

    /**
     * 分页对象
     */
    private Page page;

}
