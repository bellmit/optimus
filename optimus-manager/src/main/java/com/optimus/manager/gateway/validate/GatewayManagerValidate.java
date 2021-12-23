package com.optimus.manager.gateway.validate;

import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.RespCodeEnum;

/**
 * 网关ManagerValidate
 * 
 * @author sunxp
 */
public class GatewayManagerValidate {

    /**
     * 验证执行脚本输入对象
     * 
     * @param input
     */
    public static void validateExecuteScript(ExecuteScriptInputDTO input) {

        // 断言:非空
        AssertUtil.notEmpty(input, RespCodeEnum.GATEWAY_EXECUTE_SCRIPT_ERROR, "执行脚本输入对象不能为空");
        AssertUtil.notEmpty(input.getScriptMethod(), RespCodeEnum.GATEWAY_EXECUTE_SCRIPT_ERROR, "执行脚本方法名不能为空");
        AssertUtil.notEmpty(input.getImplPath(), RespCodeEnum.GATEWAY_EXECUTE_SCRIPT_ERROR, "执行脚本实现路径不能为空");

    }

}
