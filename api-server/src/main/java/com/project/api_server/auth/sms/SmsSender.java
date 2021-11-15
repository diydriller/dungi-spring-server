package com.project.api_server.auth.sms;

public interface SmsSender {
    void sendSms(String phoneNumber,String randomNumber);
}
