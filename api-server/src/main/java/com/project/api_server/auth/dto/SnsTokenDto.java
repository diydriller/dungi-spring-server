package com.project.api_server.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SnsTokenDto {
    private String access_token;
}
