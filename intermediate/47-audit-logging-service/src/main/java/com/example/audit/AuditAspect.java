package com.example.audit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {

    private final AuditLogService auditLogService;

    public AuditAspect(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Around("@annotation(Auditable)")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().getName();
        String arguments = Arrays.toString(joinPoint.getArgs());

        Object result = null;
        String returnValue;
        Throwable exception = null;

        try {
            result = joinPoint.proceed();
            returnValue = result != null ? result.toString() : "null";
        } catch (Throwable e) {
            exception = e;
            returnValue = "Exception: " + e.getMessage();
            throw e;
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            AuditLog auditLog = new AuditLog(methodName, arguments, returnValue, executionTime);
            auditLogService.saveAuditLog(auditLog);
        }

        return result;
    }
}
