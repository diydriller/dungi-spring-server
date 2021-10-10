package com.project.auth_server.dto;

import lombok.Data;

@Data
public class KakaoTokenDto {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String refresh_token;
    private int refresh_token_expires_in;
}
