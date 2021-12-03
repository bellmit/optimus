package com.optimus.service.order.job;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.optimus.manager.common.CommonSystemConfigManager;
import com.optimus.util.AssertUtil;
import com.optimus.util.JacksonUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.exception.OptimusException;

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

        } catch (OptimusException e) {
            log.error("加载系统配置异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
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

            // 查询系统配置
            String value = loadSystemConfig(baseKey);
            AssertUtil.notEmpty(value, RespCodeEnum.FAILE, "未配置系统定时任务参数");

            // 转换为JsonNode
            JsonNode jsonNode = JacksonUtil.toTree(value);
            JsonNode shardJsonNode = jsonNode.get(InetAddress.getLocalHost().getHostAddress());
            Integer totalShard = jsonNode.size();

            AssertUtil.notEmpty(shardJsonNode, RespCodeEnum.FAILE, "未配置系统定时任务参数");
            AssertUtil.notEmpty(totalShard, RespCodeEnum.FAILE, "未配置系统定时任务参数");

            // 分片信息
            Map<Integer, Integer> shardingMap = new HashMap<>(16);
            shardingMap.put(shardJsonNode.asInt(), totalShard);

            return shardingMap;

        } catch (OptimusException e) {
            log.error("定时任务分片异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
            return null;
        } catch (Exception e) {
            log.error("系统异常:", e);
            return null;
        }

    }

}
