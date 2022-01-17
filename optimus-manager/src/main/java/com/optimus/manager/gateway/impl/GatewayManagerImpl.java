package com.optimus.manager.gateway.impl;

import com.optimus.manager.gateway.GatewayManager;
import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.manager.gateway.validate.GatewayManagerValidate;
import com.optimus.util.AssertUtil;
import com.optimus.util.JacksonUtil;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.model.exception.OptimusException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import lombok.extern.slf4j.Slf4j;

/**
 * 网关ManagerImpl
 *
 * @author sunxp
 */
@Component
@Slf4j
public class GatewayManagerImpl implements GatewayManager {

    @Autowired
    private GroovyScriptEngine groovyScriptEngine;

    @Override
    public ExecuteScriptOutputDTO executeScript(ExecuteScriptInputDTO input) {

        log.info("执行脚本输入对象:{}", input);

        try {

            // 验证输入对象
            GatewayManagerValidate.validateExecuteScript(input);

            // 绑定参数
            Binding binding = new Binding();
            binding.setVariable("input", JacksonUtil.toString(input));

            // 执行脚本
            Object result = groovyScriptEngine.run(input.getImplPath(), binding);
            AssertUtil.notEmpty(result, RespCodeEnum.GATEWAY_EXECUTE_SCRIPT_ERROR, "执行脚本输出对象不能为空");

            // 脚本输出对象
            ExecuteScriptOutputDTO output = JacksonUtil.toBean((String) result, ExecuteScriptOutputDTO.class);
            AssertUtil.notEmpty(output.getOrderId(), RespCodeEnum.GATEWAY_EXECUTE_SCRIPT_ERROR, "执行脚本输出对象订单编号不能为空");
            AssertUtil.notEmpty(output.getOrderStatus(), RespCodeEnum.GATEWAY_EXECUTE_SCRIPT_ERROR, "执行脚本输出对象订单状态不能为空");

            log.info("执行脚本输出对象:{}", output);

            return output;

        } catch (OptimusException e) {
            log.error("执行脚本异常:", e);
            log.warn("执行脚本异常:[{}-{}:{}]", e.getRespCodeEnum().getCode(), e.getRespCodeEnum().getMemo(), e.getMemo());
            return null;
        } catch (Exception e) {
            log.error("执行脚本异常:", e);
            return null;
        }

    }

}
