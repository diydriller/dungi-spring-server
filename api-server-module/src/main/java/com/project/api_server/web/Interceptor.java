package com.project.api_server.web;

import com.project.common.error.AuthenticationException;
import com.project.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.project.common.response.BaseResponseStatus.AUTHENTICATION_ERROR;


@Component
public class Interceptor implements HandlerInterceptor {

    public static final String ACCESS_TOKEN = "access_token";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(ACCESS_TOKEN);
        if(StringUtils.isEmpty(token)){
            throw new AuthenticationException(AUTHENTICATION_ERROR);
        }
        try{
            JwtUtil.verifyToken(token);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new AuthenticationException(AUTHENTICATION_ERROR);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
