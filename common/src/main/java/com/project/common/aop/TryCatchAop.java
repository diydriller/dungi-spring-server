package com.project.common.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.common.error.BaseException;
import com.project.common.response.BaseResponse;
import com.project.common.response.BaseResponseStatus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.project.common.response.BaseResponseStatus.*;

@Aspect
@Component
public class TryCatchAop {

    @Pointcut("@annotation(com.project.common.aop.BaseExceptionAnnotation)")
    public void baseException(){};
    @Pointcut("@annotation(com.project.common.aop.IOExceptionAnnotation)")
    public void iOException(){};

    @Around("baseException()")
    public ResponseEntity<BaseResponse<?>> baseExeption(ProceedingJoinPoint jp){
        try{
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body((BaseResponse)jp.proceed());

        } catch(BaseException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse(e.getStatus()));
        } catch (Throwable e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse(SERVER_ERROR));
        }
    }

    @Around("iOException()")
    public ResponseEntity<BaseResponse<?>> iOExeption(ProceedingJoinPoint jp){
        try{
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body((BaseResponse)jp.proceed());

        } catch(IOException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse(IO_ERROR));
        }
        catch(BaseException e){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse(e.getStatus()));
        }catch (Throwable e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse(SERVER_ERROR));
        }
    }



}
