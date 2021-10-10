package com.project.auth_server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class SendSmsRequestDto {
    @NotEmpty(message = "phoneNumber is emtpy ")
    @Pattern(regexp = "\\d{11}",message = "phoneNumber format is incorrect")
    private String phoneNumber;
}
