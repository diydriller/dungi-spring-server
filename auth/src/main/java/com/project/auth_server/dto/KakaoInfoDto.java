package com.project.auth_server.dto;

import lombok.Data;

@Data
public class KakaoInfoDto {

    Account kakao_account;

    @Data
    public class Account{
        String email;
    }
}
