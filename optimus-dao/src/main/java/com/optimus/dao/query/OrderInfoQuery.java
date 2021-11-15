package com.optimus.dao.query;

import java.io.Serializable;

import com.optimus.util.page.Page;

import lombok.Data;

/**
 * 订单信息条件Query
 * 
 * @author sunxp
 */
@Data
public class OrderInfoQuery implements Serializable {

    private static final long serialVersionUID = -9075042761874255007L;

    /**
     * 会员编号
     */
    private String memberId;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 调用方订单编号
     */
    private String callerOrderId;

    /**
     * 分页对象
     */
    private Page page;

}