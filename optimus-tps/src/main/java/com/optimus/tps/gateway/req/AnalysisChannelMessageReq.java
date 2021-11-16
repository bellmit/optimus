package com.optimus.tps.gateway.req;

import java.io.Serializable;

import lombok.Data;

/**
 * 解析渠道消息Req
 * 
 * @author sunxp
 */
@Data
public class AnalysisChannelMessageReq implements Serializable {

    private static final long serialVersionUID = 7393413432717045888L;

    /**
     * 渠道消息
     */
    private String message;

    /**
     * 实现类型
     */
    private String implType;

    /**
     * 实现路径
     */
    private String implPath;

    /**
     * 业务大字段
     */
    private String bizContent;

}
