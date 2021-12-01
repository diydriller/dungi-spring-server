package com.project.api_server.application.user.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;



@Data
@NoArgsConstructor
public class CheckEmailRequestDto {
    @NotEmpty(message = "email is empty")
    @Pattern(regexp = "\\w+@\\w+.\\w+",message = "email format is wrong")
    String email;
}
