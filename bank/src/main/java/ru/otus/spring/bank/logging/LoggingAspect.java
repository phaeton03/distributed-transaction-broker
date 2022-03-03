package ru.otus.spring.bank.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.otus.spring.bank.logging.pojo.Journal;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class LoggingAspect {
    @Autowired
    private HttpServletRequest request;

    @Around("@annotation(Audit)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Audit audit = method.getAnnotation(Audit.class);

        long start = System.currentTimeMillis();
        Object object = joinPoint.proceed();

        String uri = request.getRequestURI() + '?' + request.getQueryString();
        Journal journal = new Journal(audit.value(), audit.type(), System.currentTimeMillis() - start, uri);
        log.info(journal.toString());

        return object;
    }
}