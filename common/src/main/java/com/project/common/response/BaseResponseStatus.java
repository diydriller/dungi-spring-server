package com.project.common.response;

import lombok.Getter;


@Getter
public enum BaseResponseStatus {

    SUCCESS(true, 1000, "요청에 성공하였습니다."),

    // auth
    FILE_UPLOAD_ERROR(false,2000,"파일 업로드 에러입니다."),
    ALREADY_EXISTS_EMAIL(false,2001,"중복된 이메일입니다."),
    SMS_SEND_ERROR(false,2002,"문자발송 에러입니다"),
    CODE_NOT_EQUAL(false,2003,"코드가 불일치합니다."),
    HTTP_ERROR(false,2004,"http 에러입니다."),
    NOT_EXISTS_EMAIL(false,2005,"존재하지않는 이메일입니다."),
    PASSWORD_NOT_EQUAL(false, 2006, "비밀번호가 일치하지 않습니다."),
    KAKAO_LOGIN_FAIL(false,2007,"카카오 로그인 실패입니다."),
    NOT_EXIST_USER(false,2008,"존재하지않는 유저입니다"),
    NOT_EXIST_ROOM(false,2009,"존재하지않는 방입니다"),
    NOT_EXIST_USER_ROOM(false,2010,"방에 유저가 존재하지 않습니다."),
    JSON_OBJECT_MAPPING_ERROR(false,2011,"json object 변환 에러입니다."),
    NOT_USER_LOGIN(false,2012,"로그인 되지 않은 유저입니다."),
    CODE_NOT_EXIST(false,2013,"코드가 존재하지않습니다"),
    JWT_ERROR(false,2014,"jwt 에러입니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;


    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
