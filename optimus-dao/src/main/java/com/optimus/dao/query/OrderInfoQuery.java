package com.optimus.dao.query;

import java.io.Serializable;
import java.util.Date;

import com.optimus.util.constants.order.OrderMerchantNotifyStatusEnum;
import com.optimus.util.constants.order.OrderReleaseStatusEnum;
import com.optimus.util.constants.order.OrderSplitProfitStatusEnum;
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
     * 释放状态
     * 
     * @see OrderReleaseStatusEnum
     */
    private String releaseStatus;

    /**
     * 分润状态
     * 
     * @see OrderSplitProfitStatusEnum
     */
    private String splitProfitStatus;

    /**
     * 商户通知状态
     * 
     * @see OrderMerchantNotifyStatusEnum
     */
    private String merchantNotifyStatus;

    /**
     * 回调商户次数
     */
    private Short merchantCallbackCount;

    /**
     * 网关渠道订单查询次数
     */
    private Short channelOrderQueryCount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 分页对象
     */
    private Page page;

}
