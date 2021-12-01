package com.project.common.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.model.User;

public class StringUtil {

    public static String trimPhoneNumber(String phoneNumber){
        return "+82"+phoneNumber.substring(1);
    }

    public static String randomNumber(){
        return String.valueOf(Math.random() * 9000+1000);
    }

    public static String userToJson(User user) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        return userJson;
    }

}
