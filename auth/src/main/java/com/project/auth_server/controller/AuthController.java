package com.project.auth_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.auth_server.dto.*;
import com.project.auth_server.service.AuthService;
import com.project.auth_server.service.KakaoHttpService;
import com.project.common.error.BaseException;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
import com.project.common.service.JwtService;
import com.project.common.service.RedisService;
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
    public BaseResponse join(@Valid JoinRequestDto joinRequest) throws BaseException {
        try {
            CheckEmailRequestDto emailRequestDto=new CheckEmailRequestDto(joinRequest.getEmail());
            authService.checkEmailPresent(emailRequestDto);

            authService.createUser(joinRequest);

            return new BaseResponse(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

    @PostMapping("/check/email")
    public BaseResponse checkEmail(@RequestBody @Valid CheckEmailRequestDto emailRequestDto) throws BaseException {

        try {
            authService.checkEmailPresent(emailRequestDto);

            return new BaseResponse(SUCCESS);
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

    @PostMapping(value = "/phone")
    public BaseResponse sendSms(@RequestBody @Valid SendSmsRequestDto smsRequestDto) throws BaseException {

        try {
            authService.sendSms(smsRequestDto);
            return new BaseResponse(SUCCESS);

        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }



    @PostMapping("/check/phone")
    public BaseResponse checkCode(@RequestBody @Valid CheckCodeRequestDto codeRequestDto) throws BaseException{
        try{

            authService.compareCode(codeRequestDto);

            return new BaseResponse(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

    //카카오 회원가입
    @PostMapping("/kakao/user")
    public BaseResponse kakaoJoin(@Valid KakaoJoinRequestDto joinRequestDto) throws BaseException, IOException {
        try{
            KakaoInfoDto.Account account= kakaoHttpService.getKakaoInfo(joinRequestDto.getAccess_token())
                    .getKakao_account();
            CheckEmailRequestDto emailRequestDto=new CheckEmailRequestDto(account.getEmail());
            authService.checkEmailPresent(emailRequestDto);

            authService.createKakaoUser(joinRequestDto);
            return new BaseResponse(SUCCESS);
        }
        catch (IOException e){
            return new BaseResponse(HTTP_ERROR);
        }
        catch (BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

    @GetMapping("kakao/callback")
    public BaseResponse<?> KakaoOauth(
            @RequestParam String code
    )throws BaseException {
        try{
            KakaoTokenDto tokenDto= kakaoHttpService.getKakaoToken(code);
            return new BaseResponse(tokenDto);
        }
        catch(IOException e){
            return new BaseResponse(HTTP_ERROR);
        }
        catch (BaseException e){
            return new BaseResponse(e.getStatus());
        }

    }

    // 로그인
    @PostMapping("login")
    public BaseResponse<?> login(@RequestBody @Valid LoginRequestDto requestDto) throws BaseException{
        try {
            return new BaseResponse(authService.login(requestDto));
        }
        catch (JsonProcessingException e){
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch (BaseException e){
            return new BaseResponse(e.getStatus());
        }

    }

    //카카오 로그인
    @PostMapping("/kakao/login")
    public BaseResponse<?> kakaoLogin(@RequestBody @Valid KakaoLoginRequestDto requestDto) throws BaseException{
        try {
            KakaoInfoDto.Account account= kakaoHttpService.getKakaoInfo(requestDto.getAccess_token())
                    .getKakao_account();
            return new BaseResponse(authService.kakaoLogin(requestDto,account));
        }
        catch (JsonProcessingException e){
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch (IOException e){
            return new BaseResponse(HTTP_ERROR);
        }
        catch (BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

}

