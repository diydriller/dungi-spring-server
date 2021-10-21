package com.project.auth_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.auth_server.dto.*;
import com.project.auth_server.service.AuthService;
import com.project.auth_server.service.KakaoHttpService;
import com.project.common.aop.BaseExceptionAnnotation;
import com.project.common.aop.IOExceptionAnnotation;
import com.project.common.error.BaseException;
import com.project.common.response.BaseResponse;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.io.IOException;

import static com.project.common.response.BaseResponseStatus.*;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KakaoHttpService kakaoHttpService;

    @PostMapping(value = "/user")
    @IOExceptionAnnotation
    public BaseResponse join(@Valid JoinRequestDto joinRequest) throws BaseException,IOException {
            authService.checkEmailPresent(joinRequest.getEmail());
            authService.createUser(joinRequest);
            return new BaseResponse(SUCCESS);
    }

    @PostMapping("/check/email")
    @BaseExceptionAnnotation
    public BaseResponse checkEmail(@RequestBody @Valid CheckEmailRequestDto emailRequestDto) throws BaseException {
            authService.checkEmailPresent(emailRequestDto.getEmail());
            return new BaseResponse(SUCCESS);
    }

    @PostMapping(value = "/phone")
    @BaseExceptionAnnotation
    public BaseResponse sendSms(@RequestBody @Valid SendSmsRequestDto smsRequestDto) throws BaseException {
            authService.sendSms(smsRequestDto);
            return new BaseResponse(SUCCESS);
    }



    @PostMapping("/check/phone")
    @BaseExceptionAnnotation
    public BaseResponse checkCode(@RequestBody @Valid CheckCodeRequestDto codeRequestDto) throws BaseException {
            authService.compareCode(codeRequestDto);
            return new BaseResponse(SUCCESS);
    }

    //카카오 회원가입
    @PostMapping("/kakao/user")
    @IOExceptionAnnotation
    public BaseResponse kakaoJoin(@Valid KakaoJoinRequestDto joinRequestDto) throws BaseException, IOException {

            KakaoInfoDto.Account account= kakaoHttpService.getKakaoInfo(joinRequestDto.getAccess_token())
                    .getKakao_account();
            authService.checkEmailPresent(account.getEmail());
            authService.createKakaoUser(joinRequestDto);
            return new BaseResponse(SUCCESS);
    }

    @GetMapping("kakao/callback")
    @IOExceptionAnnotation
    public BaseResponse<?> KakaoOauth(@RequestParam String code) throws BaseException, IOException {
            KakaoTokenDto tokenDto= kakaoHttpService.getKakaoToken(code);
            return new BaseResponse(tokenDto);
    }

    // 로그인
    @PostMapping("login")
    @IOExceptionAnnotation
    public BaseResponse<?> login(@RequestBody @Valid LoginRequestDto requestDto) throws BaseException,IOException {
            return new BaseResponse(authService.login(requestDto));
    }

    //카카오 로그인
    @PostMapping("/kakao/login")
    @IOExceptionAnnotation
    public BaseResponse<?> kakaoLogin(@RequestBody @Valid KakaoLoginRequestDto requestDto)
            throws BaseException,IOException {
            KakaoInfoDto.Account account= kakaoHttpService.getKakaoInfo(requestDto.getAccess_token())
                    .getKakao_account();
            return new BaseResponse(authService.kakaoLogin(requestDto,account));
    }

}

