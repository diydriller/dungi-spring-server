package com.project.api_server.perfomance.proxy;

import com.project.api_server.perfomance.PerformanceLog;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class ProxyPreparedStatementHandler implements InvocationHandler {

    private final Object preparedStatement;
    private final PerformanceLog performanceLog;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if(method.getName().equals("executeQuery")){
            final long startTime=System.currentTimeMillis();
            final Object returnValue=method.invoke(preparedStatement,args);
            performanceLog.addQueryProcessTime(System.currentTimeMillis()-startTime);
            performanceLog.addQueryCount();
            return returnValue;
        }
        return method.invoke(preparedStatement,args);
    }
}
