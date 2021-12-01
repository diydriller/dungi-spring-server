package com.project.common.error;

import com.project.common.response.BaseResponseStatus;
import lombok.Getter;

@Getter
public class AuthenticationException extends BaseException{
    public AuthenticationException(BaseResponseStatus status) {
        super(status);
    }
}
