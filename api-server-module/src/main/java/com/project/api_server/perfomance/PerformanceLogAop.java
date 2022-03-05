package com.project.api_server.perfomance;

import com.project.api_server.perfomance.extractor.RequestApi;
import com.project.api_server.perfomance.extractor.RequestPathExtractor;
import com.project.api_server.perfomance.proxy.ProxyConnectionHandler;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

@Aspect
@Component
@RequiredArgsConstructor
public class PerformanceLogAop {
    private final PerformanceLog performanceLog;
    private final RequestPathExtractor requestPathExtractor;


    @Around("execution(* javax.sql.DataSource.getConnection())")
    public Object datasource(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        final Object proceed=proceedingJoinPoint.proceed();

        return Proxy.newProxyInstance(
                proceed.getClass().getClassLoader(),
                proceed.getClass().getInterfaces(),
                new ProxyConnectionHandler(proceed,performanceLog)
        );
    }

    @Before("@within(org.springframework.web.bind.annotation.RestController) || @annotation(org.springframework.web.bind.annotation.RestController)")
    public void beforeController(JoinPoint joinPoint){
        final RequestApi requestApi= requestPathExtractor.extractRequestApi(joinPoint);
        performanceLog.setApiPath(requestApi.getPath());
        performanceLog.setApiMethod(requestApi.getMethod());
    }
}
