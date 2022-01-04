package com.optimus.manager.account.validate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.util.AssertUtil;
import com.optimus.util.constants.BaseEnum;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.order.OrderTypeEnum;
import com.optimus.util.model.exception.OptimusException;

/**
 * 账户ManagerValidate
 * 
 * @author sunxp
 */
public class AccountManagerValidate {

    /**
     * 验证账户交易入参
     * 
     * @param doTransList
     */
    public static void validateDoTrans(List<DoTransDTO> doTransList) {

        // 断言:账户交易入参
        AssertUtil.notEmpty(doTransList, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易对象不能为空");

        // 账户交易重复性
        Set<String> maSet = new HashSet<>();
        Set<String> orderIdSet = new HashSet<>();
        Set<String> orderTypeSet = new HashSet<>();

        for (DoTransDTO item : doTransList) {

            String memberId = item.getMemberId();
            String orderId = item.getOrderId();
            String orderType = item.getOrderType();
            String changeType = item.getChangeType();
            BigDecimal amount = item.getAmount();
            String remark = item.getRemark();

            // 断言:不为空
            AssertUtil.notEmpty(memberId, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易会员编号不能为空");
            AssertUtil.notEmpty(orderId, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易订单编号不能为空");
            AssertUtil.notEmpty(orderType, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易订单类型不能为空");
            AssertUtil.notEmpty(changeType, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易变更类型不能为空");
            AssertUtil.notEmpty(amount, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易金额不能为空");
            AssertUtil.notEmpty(remark, RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易备注不能为空");

            // 验证合法性
            AssertUtil.notEmpty(OrderTypeEnum.instanceOf(orderType), RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易非法的订单类型");
            AssertUtil.notEmpty(AccountChangeTypeEnum.instanceOf(changeType), RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易非法的变更类型");

            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易金额不能小于0");
            }
            if (amount.scale() > Integer.parseInt(BaseEnum.SCALE_FOUR.getCode())) {
                throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易金额精度为4");
            }

            // 验证重复性
            String ma = memberId + changeType.substring(0, changeType.length() - 1);
            if (!maSet.add(ma)) {
                throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易同一账户不允许在同一事务中");
            }

            orderIdSet.add(orderId);
            orderTypeSet.add(orderType);

        }

        // 验证重复性
        if (orderIdSet.size() != 1 || orderTypeSet.size() != 1) {
            throw new OptimusException(RespCodeEnum.ACCOUNT_TRANSACTION_ERROR, "账户交易订单编号或订单类型不合法");
        }

    }

}
