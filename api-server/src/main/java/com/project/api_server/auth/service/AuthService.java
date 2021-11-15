package com.project.api_server.auth.service;

import com.project.api_server.auth.dto.*;

public interface AuthService {
    void createUser(JoinRequestDto requestDto) throws Exception;
    void checkEmailPresent(String email) throws Exception;
    void sendSms(SendSmsRequestDto requestDto) throws Exception;
    void compareCode(CheckCodeRequestDto requestDto) throws Exception;
    void createSnsUser(SnsJoinRequestDto requestDto) throws Exception;
    String login(LoginRequestDto requestDto) throws Exception;
    String snsLogin(SnsLoginRequestDto requestDto,String email) throws Exception;
}
