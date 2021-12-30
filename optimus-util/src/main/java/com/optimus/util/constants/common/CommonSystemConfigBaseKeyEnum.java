package com.optimus.util.constants.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统配置键Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum CommonSystemConfigBaseKeyEnum {

    /* ---------系统配置键--------- */

    BASE_NOTICE_URL("BASE_NOTICE_URL", "订单通知地址"),

    GROOVY_SCRIPT_PATH("GROOVY_SCRIPT_PATH", "GROOVY脚本路径"),

    MERCHANT_CALLBACK_JOB_SHARDING("MERCHANT_CALLBACK_JOB_SHARDING", "回调商户定时任务分片"),

    MERCHANT_CALLBACK_COUNT("MERCHANT_CALLBACK_COUNT", "回调商户次数"),

    MERCHANT_CALLBACK_INTERVAL("MERCHANT_CALLBACK_INTERVAL", "回调商户间隔"),

    CHANNEL_ORDER_QUERY_JOB_SHARDING("CHANNEL_ORDER_QUERY_JOB_SHARDING", "渠道订单查询定时任务分片"),

    CHANNEL_ORDER_QUERY_COUNT("CHANNEL_ORDER_QUERY_COUNT", "渠道订单查询次数"),

    CHANNEL_ORDER_QUERY_INTERVAL("CHANNEL_ORDER_QUERY_INTERVAL", "渠道订单查询间隔"),

    RELEASE_ORDER_JOB_SHARDING("RELEASE_ORDER_JOB_SHARDING", "释放订单定时任务分片"),

    RELEASE_ORDER_INTERVAL("RELEASE_ORDER_INTERVAL", "释放订单间隔"),

    SPLIT_PROFIT_JOB_SHARDING("SPLIT_PROFIT_JOB_SHARDING", "订单分润定时任务分片"),

    SPLIT_PROFIT_INTERVAL("SPLIT_PROFIT_INTERVAL", "订单分润间隔"),

    ;

    private String code;
    private String memo;
}
