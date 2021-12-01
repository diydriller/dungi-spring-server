package com.project.api_server.application.user.controller;

import com.project.api_server.application.user.dto.*;
import com.project.api_server.domain.user.service.UserService;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
import com.project.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.project.common.response.BaseResponseStatus.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private String LOGIN_USER="login_user";

    // 유저 생성
    @PostMapping(value = "/user")
    public BaseResponse join(@Valid JoinRequestDto request) throws Exception {
            userService.createUser(request);
            return new BaseResponse(SUCCESS);
    }

   // 이메일 중복 여부
    @PostMapping("/check/email")
    public BaseResponse checkEmail(@RequestBody @Valid CheckEmailRequestDto requestDto) throws Exception {
            userService.checkEmailPresent(requestDto.getEmail());
            return new BaseResponse(SUCCESS);
    }

    // sms 전송
    @PostMapping(value = "/phone")
    public BaseResponse sendSms(@RequestBody @Valid SendSmsRequestDto requestDto) throws Exception {
            userService.sendSms(requestDto);
            return new BaseResponse(SUCCESS);
    }

    // sms 인증번호 검증
    @PostMapping("/check/phone")
    public BaseResponse checkCode(@RequestBody @Valid CheckCodeRequestDto requestDto) throws Exception {
            userService.compareCode(requestDto);
            return new BaseResponse(SUCCESS);
    }

    // 카카오 유저생성
    @PostMapping("/kakao/user")
    public BaseResponse kakaoJoin(@Valid SnsJoinRequestDto requestDto) throws Exception {
            userService.createSnsUser(requestDto);
            return new BaseResponse(SUCCESS);
    }

    @GetMapping("kakao/callback")
    public BaseResponse<?> kakaoOauth(@RequestParam String code) throws Exception {
            return new BaseResponse(userService.snsToken(code));
    }

    // 로그인
    @PostMapping("login")
    public BaseResponse<?> login(@RequestBody @Valid LoginRequestDto requestDto, HttpSession session)
            throws Exception {
        User user = userService.login(requestDto);
        session.setAttribute(LOGIN_USER,user);
        return new BaseResponse(JwtUtil.getToken(user));
    }

    // 카카오 로그인
    @PostMapping("/kakao/login")
    public BaseResponse<?> kakaoLogin(@RequestBody @Valid SnsLoginRequestDto requestDto,HttpSession session)
            throws Exception {
        User user = userService.snsLogin(requestDto);
        session.setAttribute(LOGIN_USER,user);
        return new BaseResponse(JwtUtil.getToken(user));
    }

}

