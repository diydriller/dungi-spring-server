package com.project.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import static com.project.common.response.BaseResponseStatus.SUCCESS;


@Data
@Builder
@AllArgsConstructor
public class BaseResponse<T> {

    private final Boolean isSuccess;
    private final String message;
    private final int code;
    private T data;

    // 요청에 성공한 경우
    public BaseResponse(T data) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.data = data;
    }

    // 요청에 실패한 경우
    public BaseResponse(BaseResponseStatus status) {
        this.isSuccess = status.isSuccess();
        this.message = status.getMessage();
        this.code = status.getCode();
    }


}

