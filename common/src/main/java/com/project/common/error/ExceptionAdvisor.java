package com.project.common.error;

import com.project.common.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.validation.BindException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionAdvisor {

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



    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseBody
    public BaseResponse handleMissingHeaderException(MissingRequestHeaderException ex){

        return BaseResponse.builder()
                .isSuccess(false)
                .message("권한이 없습니다")
                .code(403)
                .build();
    }

}
