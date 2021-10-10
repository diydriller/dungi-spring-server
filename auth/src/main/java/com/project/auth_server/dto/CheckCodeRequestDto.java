package com.project.auth_server.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class CheckCodeRequestDto {

    @NotEmpty(message = "code is empty")
    @Pattern(regexp = "\\d{4}",message = "code format is incorrect")
    private String code;

    @NotEmpty(message = "phoneNumber is emtpy ")
    @Pattern(regexp = "\\d{11}",message = "phoneNumber format is incorrect")
    private String phoneNumber;
}
