package com.project.common.util;

public class StringUtil {

    public static String redisLogin(Long id){
        return "login_"+id;
    }
    public static String trimPhoneNumber(String phoneNumber){
        return "+82"+phoneNumber.substring(1);
    }

    public static String randomNumber(){ return String.valueOf(Math.random() * 9000+1000);}

}
