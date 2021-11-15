package com.project.api_server.auth.dto;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class SnsInfoDto {

    Account kakao_account;

    @Data
    public class Account{
        String email;
    }
}
