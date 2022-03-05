package com.project.api_server.perfomance.proxy;

import com.project.api_server.perfomance.PerformanceLog;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@RequiredArgsConstructor
public class ProxyConnectionHandler implements InvocationHandler {

    private final Object connection;
    private final PerformanceLog performanceLog;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Object returnValue=method.invoke(connection,args);
        if(method.getName().equals("prepareStatement")){
            return Proxy.newProxyInstance(
                    returnValue.getClass().getClassLoader(),
                    returnValue.getClass().getInterfaces(),
                    new ProxyPreparedStatementHandler(returnValue,performanceLog)
            );
        }
        return returnValue;
    }
}
