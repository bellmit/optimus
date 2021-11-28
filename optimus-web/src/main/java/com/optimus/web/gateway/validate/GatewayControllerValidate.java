package com.optimus.web.gateway.validate;

import com.optimus.manager.gateway.dto.ExecuteScriptOutputDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.BaseEnum;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.order.OrderStatusEnum;
import com.optimus.util.exception.OptimusException;

/**
 * 网关渠道ControllerValidate
 * 
 * @author sunxp
 */
public class GatewayControllerValidate {

    /**
     * 验证执行脚本入参
     * 
     * @param output
     */
    public static void validateChannelCallback(ExecuteScriptOutputDTO output) {

        // 断言:非空
        AssertUtil.notEmpty(output, RespCodeEnum.INVALID_PARAM, "执行脚本输出对象不能为空");
        AssertUtil.notEmpty(output.getOrderId(), RespCodeEnum.INVALID_PARAM, "订单编号不能为空");
        AssertUtil.notEmpty(output.getCalleeOrderId(), RespCodeEnum.INVALID_PARAM, "被调用方订单编号不能为空");
        AssertUtil.notEmpty(output.getOrderStatus(), RespCodeEnum.INVALID_PARAM, "订单状态不能为空");
        AssertUtil.notEmpty(output.getAmount(), RespCodeEnum.INVALID_PARAM, "订单金额不能为空");
        AssertUtil.notEmpty(output.getActualAmount(), RespCodeEnum.INVALID_PARAM, "订单实际金额不能为空");

        // 订单状态:成功
        AssertUtil.notEquals(OrderStatusEnum.ORDER_STATUS_AP.getCode(), output.getOrderStatus(), RespCodeEnum.INVALID_PARAM, "订单状态必须为成功");

        // 金额合法性
        if (output.getAmount().scale() > Integer.parseInt(BaseEnum.SCALE_TWO.getCode())) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "订单金额精度为2");
        }
        if (output.getActualAmount().scale() > Integer.parseInt(BaseEnum.SCALE_TWO.getCode())) {
            throw new OptimusException(RespCodeEnum.INVALID_PARAM, "订单实际金额精度为2");
        }

    }
}
