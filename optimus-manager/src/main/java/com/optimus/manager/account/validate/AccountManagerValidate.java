package com.optimus.manager.account.validate;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.optimus.manager.account.dto.DoTransDTO;
import com.optimus.util.constants.account.AccountChangeTypeEnum;
import com.optimus.util.constants.order.OrderTypeEnum;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 账户Manager验证
 * 
 * @author sunxp
 */
public class AccountManagerValidate {

    /**
     * 验证账户交易入参
     * 
     * @param doTransList
     * @return
     */
    public static String validateDoTrans(List<DoTransDTO> doTransList) {

        if (CollectionUtils.isEmpty(doTransList)) {
            return "账户交易入参不能为空";
        }

        // 账户交易重复性
        Set<String> maSet = new HashSet<>();

        for (DoTransDTO item : doTransList) {

            String memberId = item.getMemberId();
            String orderId = item.getOrderId();
            String orderType = item.getOrderType();
            String changeType = item.getChangeType();
            BigDecimal amount = item.getAmount();
            String remark = item.getRemark();

            // 非空验证
            if (!StringUtils.hasLength(memberId)) {
                return "账户交易会员编号不能为空";
            }
            if (!StringUtils.hasLength(orderId)) {
                return "账户交易订单编号不能为空";
            }
            if (!StringUtils.hasLength(orderType)) {
                return "账户交易订单类型不能为空";
            }
            if (!StringUtils.hasLength(changeType)) {
                return "账户交易变更类型不能为空";
            }
            if (amount == null) {
                return "账户交易金额不能为空";
            }
            if (!StringUtils.hasLength(remark)) {
                return "账户交易备注不能为空";
            }

            // 合法性验证
            if (OrderTypeEnum.instanceOf(orderType) == null) {
                return "账户交易非法的订单类型";
            }
            if (AccountChangeTypeEnum.instanceOf(changeType) == null) {
                return "账户交易非法的变更类型";
            }
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                return "账户交易金额不能小于0";
            }
            if (amount.scale() > 4) {
                return "账户交易金额精度为4";
            }

            // 重复性验证
            String ma = memberId + changeType.substring(0, changeType.length() - 1);
            if (!maSet.add(ma)) {
                return "账户交易同一账户不允许在同一事务中";
            }

        }

        return null;
    }

}
