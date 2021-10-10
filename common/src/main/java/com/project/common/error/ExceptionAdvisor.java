package com.project.common.error;

import com.project.common.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
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



/*
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public BaseResponse handleValidationException(NoHandlerFoundException ex){

        return BaseResponse.builder()
                .isSuccess(false)
                .message(ex.getMessage())
                .status(404)
                .build();
    }
*/
}
