package com.kaede.erp.common.aspect;


import com.kaede.erp.common.annotation.OperationLog;
import com.kaede.erp.common.context.UserContext;
import com.kaede.erp.mapper.OperationLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;


@Aspect
@Component
public class OperationLogAspect {


    private final OperationLogMapper logMapper;


    public OperationLogAspect(OperationLogMapper logMapper) {
        this.logMapper = logMapper;
    }


    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {

        long start = System.currentTimeMillis();

        String result = "SUCCESS";
        Object proceed;

        try {

            proceed = joinPoint.proceed();

        } catch (Exception e) {

            result = "FAIL";
            throw e;

        } finally {

            long duration = System.currentTimeMillis() - start;

            HttpServletRequest request = null;

            try {
                request = ((ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes()).getRequest();
            } catch (Exception ignored) {
            }


            com.kaede.erp.entity.OperationLog log =
                    new com.kaede.erp.entity.OperationLog();

            log.setUserId(UserContext.getUserId());
            log.setModule(operationLog.module());
            log.setOperation(operationLog.operation());
            log.setDescription(operationLog.description());

            if (request != null) {
                log.setRequestUrl(request.getRequestURI());
                log.setRequestMethod(request.getMethod());
                log.setIp(request.getRemoteAddr());
                log.setRequestParams(
                        Arrays.toString(joinPoint.getArgs())
                );
            }

            log.setResult(result);
            log.setDurationMs(duration);
            log.setCreateTime(LocalDateTime.now());

            logMapper.insert(log);

        }

        return proceed;
    }

}
