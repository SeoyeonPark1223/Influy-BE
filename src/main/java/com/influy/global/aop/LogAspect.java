package com.influy.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Pointcut("execution(* com.influy.domain..controller..*(..))")
    private void cut(){}

    @Around("cut()")
    public Object logRecord(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 정보
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info("[START] ======= Method name = {} =======", method.getName());

        // 메소드 수행 시간
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        // 메소드 파라미터값 로깅
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) log.info("No parameter");
        for (Object arg : args) {
            log.info("Parameter type = {}", arg.getClass().getSimpleName());
            log.info("Parameter value = {}", arg);
        }

        // 메서드 리턴값 로깅
        log.info("Return type = {}", result.getClass().getSimpleName());
        log.info("Return result = {}", result);

        long executionTime = System.currentTimeMillis() - startTime;

        log.info("[END] ======= Method executionTime = {} =======", executionTime);

        return result;
    }
}
