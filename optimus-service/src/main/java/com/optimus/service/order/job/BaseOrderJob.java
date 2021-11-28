package com.optimus.service.order.job;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.optimus.manager.common.CommonSystemConfigManager;
import com.optimus.util.AssertUtil;
import com.optimus.util.JacksonUtil;
import com.optimus.util.constants.RespCodeEnum;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 * 订单定时任务
 * 
 * @author sunxp
 */
@Slf4j
public abstract class BaseOrderJob {

    @Autowired
    private CommonSystemConfigManager commonSystemConfigManager;

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

            return commonSystemConfigManager.getCommonSystemConfigByBaseKey(baseKey);

        } catch (Exception e) {
            log.error("加载系统配置,异常:{}", e);
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

            // 查询系统配置
            String value = loadSystemConfig(baseKey);
            AssertUtil.notEmpty(value, RespCodeEnum.FAILE, "系统定时任务参数未配置");

            // 转换为JsonNode
            JsonNode jsonNode = JacksonUtil.toTree(value);
            Integer shard = jsonNode.get(InetAddress.getLocalHost().getHostAddress()).asInt();
            Integer totalShard = jsonNode.size();

            AssertUtil.notEmpty(shard, RespCodeEnum.FAILE, "系统定时任务参数未配置");
            AssertUtil.notEmpty(totalShard, RespCodeEnum.FAILE, "系统定时任务参数未配置");

            // 分片信息
            Map<Integer, Integer> shardingMap = new HashMap<>(16);
            shardingMap.put(shard, totalShard);

            return shardingMap;

        } catch (Exception e) {
            log.error("未配置定时任务分片,异常:{}", e);
            return null;
        }

    }

}
