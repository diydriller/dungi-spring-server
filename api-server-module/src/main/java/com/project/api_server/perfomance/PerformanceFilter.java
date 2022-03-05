package com.project.api_server.perfomance;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.*;
import java.io.IOException;



@Slf4j
@RequiredArgsConstructor
public class PerformanceFilter implements Filter {

    private final PerformanceLog performanceLog;
    private final Logger logger= LoggerFactory.getLogger(PerformanceLog.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final long startTime=System.currentTimeMillis();
        performanceLog.setRequestTime(startTime);
        chain.doFilter(request,response);
        performanceLog.setRequestProcessTime(System.currentTimeMillis()-startTime);

        logger.info(String.valueOf(performanceLog));
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
