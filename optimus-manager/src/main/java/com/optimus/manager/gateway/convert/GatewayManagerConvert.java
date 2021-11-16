package com.optimus.manager.gateway.convert;

import com.optimus.manager.gateway.dto.ExecuteScriptInputDTO;
import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.tps.gateway.req.ExecuteScriptReq;
import com.optimus.tps.gateway.resp.ExecuteScriptResp;

import org.springframework.beans.BeanUtils;

/**
 * 网关Manager转换器
 * 
 * @author sunxp
 */
public class GatewayManagerConvert {

    /**
     * 获取执行脚本请求对象
     * 
     * @param input
     * @return
     */
    public static ExecuteScriptReq getExecuteScriptReq(ExecuteScriptInputDTO input) {

        ExecuteScriptReq req = new ExecuteScriptReq();
        BeanUtils.copyProperties(input, req);

        return req;

    }

    /**
     * 获取脚本输出传输对象
     * 
     * @param resp
     * @return
     */
    public static ExecuteScriptOutputDTO getExecuteScriptOutputDTO(ExecuteScriptResp resp) {

        ExecuteScriptOutputDTO output = new ExecuteScriptOutputDTO();
        BeanUtils.copyProperties(resp, output);

        return output;

    }

}
