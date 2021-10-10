package com.project.common.error;

import com.project.common.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseException extends RuntimeException {

    private BaseResponseStatus status;
}
