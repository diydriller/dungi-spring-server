package com.project.api_server.application.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SnsTokenDto {
    private String access_token;
}
