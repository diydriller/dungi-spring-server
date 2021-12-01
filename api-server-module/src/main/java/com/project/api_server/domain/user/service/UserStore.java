package com.project.api_server.domain.user.service;

import com.project.common.model.User;

public interface UserStore {
    void sendSms(String phoneNumber,String randomNumber);
    void saveUser(User user);
    void checkEmailPresent(String email);
    User findUserByEmail(String email);
    void saveCode(String number,String code);
    String getCode(String number);
}
