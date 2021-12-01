package com.project.api_server.web;

import com.project.common.error.AuthenticationException;
import com.project.common.error.BaseException;
import com.project.common.response.BaseResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.List;

import static com.project.common.response.BaseResponseStatus.SERVER_ERROR;

@RestControllerAdvice
public class ExceptionAdvisor {

    // 입력값 검증 에러 핸들러
    @Order(value=2)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public BaseResponse handleValidationException(BindException ex){
        BindingResult result = ex.getBindingResult();
        List<String> errorList = new ArrayList<>();
        result.getFieldErrors().forEach((fieldError) -> {
            errorList.add(fieldError.getField()+" : "+fieldError.getDefaultMessage() +" : rejected value is "+fieldError.getRejectedValue());
        });

        return BaseResponse.builder()
                .isSuccess(false)
                .message(String.join(" / ",errorList))
                .code(400)
                .build();
    }

    @Order(value=1)
    // 토큰이 없거나 검증 실패시 에러 핸들러
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public BaseResponse handleAuthenticationException(AuthenticationException ex){
        return new BaseResponse(ex.getStatus());
    }

    @Order(value=3)
    // 비즈니스 로직 에러 핸들러
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BaseException.class)
    @ResponseBody
    public BaseResponse handleBaseException(BaseException ex){
        return new BaseResponse(ex.getStatus());
    }

    @Order(value=4)
    // 서버 에러 핸들러
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse handleException(BaseException ex){
        return new BaseResponse(SERVER_ERROR);
    }



}
