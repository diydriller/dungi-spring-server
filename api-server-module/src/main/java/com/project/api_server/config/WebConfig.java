package com.project.api_server.config;

import com.project.api_server.web.Interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private String[] INTERCEPTOR_EXCLUDE_LIST={
            "/check/email","/phone","/check/phone","/user","/login",
            "/kakao/user","/kakao/callback","/kakao/login"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Interceptor())
                .addPathPatterns("/*")
                .excludePathPatterns(INTERCEPTOR_EXCLUDE_LIST);
    }
}