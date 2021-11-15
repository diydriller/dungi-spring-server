package com.project.common.aop;


import com.project.common.error.BaseException;
import com.project.common.response.BaseResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import static com.project.common.response.BaseResponseStatus.*;

@Aspect
@Component
public class TryCatchAop {

    // 코드가 작동하는 실행시점을 결정하는 표현식을 사용한다.
    @Pointcut("@annotation(com.project.common.aop.BaseExceptionResponseAnnotation)")
    public void baseExceptionResponse(){};

    // 전후로 코드 실행
    // catch 문은 하위 exception 부터 작성
    @Around("baseExceptionResponse()")
    public BaseResponse<?> baseExeptionResponse(ProceedingJoinPoint jp){
        try{
            return (BaseResponse)jp.proceed();
        } catch(BaseException e){
            e.printStackTrace();
            return new BaseResponse(e.getStatus());
        } catch(Exception e){
          e.printStackTrace();
          return new BaseResponse(SERVER_ERROR);
        } catch (Throwable e) {
            e.printStackTrace();
            return new BaseResponse(SERVER_ERROR);
        }
    }
}
