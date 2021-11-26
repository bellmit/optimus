package com.optimus.util.constants.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单释放状态Enum
 * 
 * @author sunxp
 */
@Getter
@AllArgsConstructor
public enum OrderReleaseStatusEnum {

    /* ---------订单释放状态--------- */

    RELEASE_STATUS_D("D", "无需释放"),

    RELEASE_STATUS_N("N", "待释放"),

    RELEASE_STATUS_Y("Y", "已释放"),

    ;

    private String code;
    private String memo;

}
