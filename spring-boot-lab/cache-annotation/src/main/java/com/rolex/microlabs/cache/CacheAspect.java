/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.microlabs.cache;

import com.rolex.microlabs.cache.annotation.Cache;
import com.rolex.microlabs.dao.RedisDao;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author rolex
 * @since 2019
 */
@Component
@Aspect
@Slf4j
public class CacheAspect {

    @Autowired
    RedisDao redisDao;

    @Around("@annotation(com.rolex.microlabs.cache.annotation.Cache)")
    public Object point(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = null;
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = pjp.getTarget().getClass().getMethod(signature.getName(), signature.getMethod().getParameterTypes());
        Cache cache = method.getAnnotation(Cache.class);
        String keyEL = cache.key();
        Class returnType = cache.returnType();
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(keyEL);
        EvaluationContext context = new StandardEvaluationContext();
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discoverer.getParameterNames(method);
        Object[] values = pjp.getArgs();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], values[i]);
        }
        String key = expression.getValue(context).toString();
        obj = redisDao.get(key, returnType);
        if (obj == null) {
            log.info("AOP: Before");
            obj = pjp.proceed();
            log.info("AOP: After");
            if (obj != null) {
                method.getReturnType();
                redisDao.put(key, obj);
                log.info("put into redis: key={}", key);
            }
        }
        return obj;
    }
}
