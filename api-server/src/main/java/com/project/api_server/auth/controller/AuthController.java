package com.project.api_server.auth.controller;

import com.project.api_server.auth.dto.*;
import com.project.api_server.auth.service.AuthService;
import com.project.api_server.auth.sns.SnsHttpService;
import com.project.common.aop.BaseExceptionResponseAnnotation;
import com.project.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

import static com.project.common.response.BaseResponseStatus.SUCCESS;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SnsHttpService snsHttpService;

    // 이메일 중복 확인 - 유저 생성
    @PostMapping(value = "/user")
    public BaseResponse join(@Valid JoinRequestDto joinRequest) throws Exception {
            authService.checkEmailPresent(joinRequest.getEmail());
            authService.createUser(joinRequest);
            return new BaseResponse(SUCCESS);
    }

    // 이메일 중복 확인
    @PostMapping("/check/email")
    @BaseExceptionResponseAnnotation
    public BaseResponse checkEmail(@RequestBody @Valid CheckEmailRequestDto emailRequestDto) throws Exception {
            authService.checkEmailPresent(emailRequestDto.getEmail());
            return new BaseResponse(SUCCESS);
    }

    // sms 전송
    @PostMapping(value = "/phone")
    @BaseExceptionResponseAnnotation
    public BaseResponse sendSms(@RequestBody @Valid SendSmsRequestDto smsRequestDto) throws Exception {
            authService.sendSms(smsRequestDto);
            return new BaseResponse(SUCCESS);
    }

    // sms 인증번호 확인
    @PostMapping("/check/phone")
    @BaseExceptionResponseAnnotation
    public BaseResponse checkCode(@RequestBody @Valid CheckCodeRequestDto codeRequestDto) throws Exception {
            authService.compareCode(codeRequestDto);
            return new BaseResponse(SUCCESS);
    }

    //카카오 계정 정보 가져오기 - 이메일 중복 확인 - 유저생성
    @PostMapping("/kakao/user")
    @BaseExceptionResponseAnnotation
    public BaseResponse kakaoJoin(@Valid SnsJoinRequestDto joinRequestDto) throws Exception {

            String email = snsHttpService.getSnsInfo(joinRequestDto.getAccess_token());
            authService.checkEmailPresent(email);
            authService.createSnsUser(joinRequestDto);
            return new BaseResponse(SUCCESS);
    }

    @GetMapping("kakao/callback")
    @BaseExceptionResponseAnnotation
    public BaseResponse<?> kakaoOauth(@RequestParam String code) throws Exception {
            SnsTokenDto tokenDto= snsHttpService.getSnsToken(code);
            return new BaseResponse(tokenDto);
    }

    // 로그인
    @PostMapping("login")
    @BaseExceptionResponseAnnotation
    public BaseResponse<?> login(@RequestBody @Valid LoginRequestDto requestDto)
            throws Exception {
            return new BaseResponse(authService.login(requestDto));
    }

    //카카오 계정정보 가져오기 - 로그인
    @PostMapping("/kakao/login")
    @BaseExceptionResponseAnnotation
    public BaseResponse<?> kakaoLogin(@RequestBody @Valid SnsLoginRequestDto requestDto)
            throws Exception {
            String email = snsHttpService.getSnsInfo(requestDto.getAccess_token());
            return new BaseResponse(authService.snsLogin(requestDto,email));
    }

}

