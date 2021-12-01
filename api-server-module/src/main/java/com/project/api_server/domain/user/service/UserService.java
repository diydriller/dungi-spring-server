package com.project.api_server.domain.user.service;

import com.project.api_server.application.user.dto.*;
import com.project.common.model.User;

public interface UserService {
    void createUser(JoinRequestDto requestDto) throws Exception;
    void checkEmailPresent(String email) throws Exception;
    void sendSms(SendSmsRequestDto requestDto) throws Exception;
    void compareCode(CheckCodeRequestDto requestDto) throws Exception;
    void createSnsUser(SnsJoinRequestDto requestDto) throws Exception;
    User login(LoginRequestDto requestDto) throws Exception;
    User snsLogin(SnsLoginRequestDto requestDto) throws Exception;
    SnsTokenDto snsToken(String code) throws Exception;
}
