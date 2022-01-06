package com.optimus.runner.aspect;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;
import com.optimus.util.AssertUtil;
import com.optimus.util.annotation.OptimusRateLimiter;
import com.optimus.util.constants.RespCodeEnum;
import com.optimus.util.model.exception.OptimusException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * 自定义限流器切面类
 * 
 * @author sunxp
 */
@Aspect
@Component
public class OptimusRateLimiterAspect {

    /** 方法:令牌桶 */
    private static final Map<String, RateLimiter> MAP = new ConcurrentHashMap<>();

    /**
     * 切入点:OptimusRateLimiter
     */
    @Pointcut("@annotation(com.optimus.util.annotation.OptimusRateLimiter)")
    public void pointCut() {
    }

    /**
     * 环绕
     * 
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 获取切面方法
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = joinPoint.getSignature().getName();

        // 获取注解对象
        OptimusRateLimiter limiter = method.getAnnotation(OptimusRateLimiter.class);
        AssertUtil.notEmpty(limiter, RespCodeEnum.ERROR_LIMIT, "服务繁忙");

        // 创建限流器
        if (!MAP.containsKey(methodName)) {
            MAP.put(methodName, RateLimiter.create(limiter.permits()));
        }

        // 获取令牌
        RateLimiter rateLimiter = MAP.get(methodName);
        if (rateLimiter.tryAcquire(limiter.timeout(), TimeUnit.SECONDS)) {
            return joinPoint.proceed();
        }

        throw new OptimusException(RespCodeEnum.ERROR_LIMIT, "服务繁忙");

    }

}
