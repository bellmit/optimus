package com.optimus.service.order.job;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.databind.JsonNode;
import com.optimus.manager.common.CommonSystemConfigManager;
import com.optimus.util.AssertUtil;
import com.optimus.util.JacksonUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单定时任务
 * 
 * @author sunxp
 */
@Slf4j
public abstract class BaseOrderJob {

    /** 基础页面大小 */
    public static final Integer BASE_ORDER_JOB_PAGE_SIZE = 1000;

    @Autowired
    private CommonSystemConfigManager commonSystemConfigManager;

    private static String ip;

    static {

        try {

            ip = InetAddress.getLocalHost().getHostAddress();
            log.info("订单定时任务,服务器IP:{}", ip);

        } catch (Exception e) {
            log.error("系统异常:", e);
        }

    }

    /**
     * 执行定时任务
     */
    public abstract void execute();

    /**
     * 加载系统配置
     * 
     * @param baseKey
     * @return
     */
    public String loadSystemConfig(String baseKey) {

        try {

            return commonSystemConfigManager.getCommonSystemConfigForSystem(baseKey);

        } catch (OptimusException e) {
            log.warn("加载系统配置异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
            return null;
        } catch (Exception e) {
            log.error("加载系统配置异常:", e);
            return null;
        }

    }

    /**
     * 分片
     * 
     * @param baseKey
     * @return {分片,分片总数}
     */
    public Map<Integer, Integer> sharding(String baseKey) {

        try {

            // 初始化IP
            if (!StringUtils.hasLength(ip)) {
                ip = InetAddress.getLocalHost().getHostAddress();
                log.info("订单定时任务,服务器IP:{}", ip);
            }

            // 查询系统配置
            String value = loadSystemConfig(baseKey);
            AssertUtil.notEmpty(value, RespCodeEnum.FAILE, "未配置系统定时任务分片");

            // 转换为JsonNode
            JsonNode jsonNode = JacksonUtil.toTree(value);
            JsonNode shardJsonNode = jsonNode.get(ip);
            Integer totalShard = jsonNode.size();

            // 未分配分片
            if (Objects.isNull(shardJsonNode) || Objects.isNull(totalShard)) {
                return null;
            }

            // 分片信息
            Map<Integer, Integer> shardingMap = new HashMap<>(16);
            shardingMap.put(shardJsonNode.asInt(), totalShard);

            return shardingMap;

        } catch (OptimusException e) {
            log.warn("定时任务分片异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
            return null;
        } catch (Exception e) {
            log.error("系统异常:", e);
            return null;
        }

    }

}
