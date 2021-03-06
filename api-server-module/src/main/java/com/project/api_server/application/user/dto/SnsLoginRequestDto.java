package com.project.api_server.application.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@Data
@NoArgsConstructor
public class SnsLoginRequestDto {

    @NotEmpty(message = "email is empty")
    @Pattern(regexp = "\\w+@\\w+.\\w+",message = "email format is wrong")
    private String email;

    @NotEmpty(message = "token is empty")
    private String access_token;

}
