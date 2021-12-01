package com.project.api_server.web;

import com.project.common.error.AuthenticationException;
import com.project.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.project.common.response.BaseResponseStatus.AUTHENTICATION_ERROR;

@Slf4j
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
            throw new AuthenticationException(AUTHENTICATION_ERROR);
        }
        return true;
    }
}
