package com.example.user.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.userservice.service.*.*(..))")
    public void serviceLayer() {
    }

    @Before("serviceLayer()")
    public void logMethodEntry(JoinPoint joinPoint) {
        log.info(">> Entering {}.{}() with args={}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void logMethodExit(JoinPoint joinPoint, Object result) {
        log.info("<< Exiting {}.{}() with result = {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                result);
    }

    @AfterThrowing(pointcut = "serviceLayer()", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Exception ex) {
        log.warn("!! Exception in {}.{}(): {}",
                joinPoint.getTarget().getClass().getSimpleName(),
                joinPoint.getSignature().getName(),
                ex.getMessage());
    }
}
