package com.project.auth_server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class KakaoLoginRequestDto {

    @NotEmpty(message = "email is empty")
    @Pattern(regexp = "\\w+@\\w+.\\w+",message = "email format is wrong")
    private String email;

    @NotEmpty(message = "token is empty")
    private String access_token;

}
