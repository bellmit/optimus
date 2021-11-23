package com.optimus.util.constants.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单商户通知状态Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum OrderMerchantNotifyStatusEnum {

    /* ---------订单商户通知状态--------- */

    ORDER_MERCHANT_NOTIFY_STATUS_NN("NN", "未通知"),

    ORDER_MERCHANT_NOTIFY_STATUS_NS("NS", "通知成功"),

    ORDER_MERCHANT_NOTIFY_STATUS_NF("NF", "通知失败"),

    ;

    private String code;
    private String memo;

}
