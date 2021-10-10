package com.project.auth_server.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.project.auth_server.dto.*;
import com.project.common.error.BaseException;

import com.project.common.model.User;
import com.project.common.repository.UserRepository;
import com.project.common.service.JwtService;
import com.project.common.service.RedisService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static com.project.common.response.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${file.upload.path}")
    private String fileUploadPath;
    @Value("${file.down.path}")
    private String fileDownPath;

    @Value("${twilio.accountId}")
    private String twilioAccountSid;
    @Value("${twilio.authToken}")
    private String twilioAuthToken;
    @Value("${twilio.adminPhoneNumber}")
    private String adminPhoneNumber;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final JwtService jwtService;

    // 유저 생성
    @Transactional
    public void createUser(JoinRequestDto requestDto) throws BaseException{

        String imageUploadUrl=imageUpload(requestDto.getImg());
        String hashedPassword=passwordEncoder.encode(requestDto.getPassword());

        userRepository.save(new User(requestDto.getEmail(),requestDto.getNickname(),
                requestDto.getEmail(),hashedPassword,requestDto.getPhoneNumber(),
                imageUploadUrl,"local"));
    }

    // 이메일 있는지 확인
    public void checkEmailPresent(CheckEmailRequestDto emailRequestDto) throws BaseException{
        userRepository.findByEmail(emailRequestDto.getEmail())
                .ifPresent(m->{throw new BaseException(ALREADY_EXISTS_EMAIL);});
    }


    // sms 문자 보내기
    @Transactional(rollbackFor = Exception.class)
    public void sendSms(SendSmsRequestDto phoneRequestDto) throws BaseException{

        int randomNumber = (int) (Math.random() * 9000) + 1000;
        redisService.setStringOpsTtl(phoneRequestDto.getPhoneNumber()
                ,String.valueOf(randomNumber), 90, TimeUnit.SECONDS);

        try {
            String trimedPhoneNumber = trimPhonNumber(phoneRequestDto.getPhoneNumber());

            Twilio.init(twilioAccountSid, twilioAuthToken);
            Message.creator(
                    new PhoneNumber(trimedPhoneNumber),
                    new PhoneNumber(adminPhoneNumber),
                    "enter the number : "+randomNumber)
                    .create();
        }
        catch (BaseException e){
            throw new BaseException(SMS_SEND_ERROR);
        }
    }


    // redis에 저장된 코드와 비교
    public void compareCode(CheckCodeRequestDto codeRequestDto) throws BaseException{

        String savedCode=redisService
                .getStringOps(trimPhonNumber(codeRequestDto.getPhoneNumber()))
                .map(Object::toString)
                .orElseThrow(()->{throw new BaseException(CODE_NOT_EXIST);});

        if(savedCode.equals(codeRequestDto.getCode())){
            throw new BaseException(CODE_NOT_EQUAL);
        }
    }

    // 카카오 회원가입
    @Transactional
    public void createKakaoUser(KakaoJoinRequestDto joinRequestDto) throws BaseException{
        String imageUploadUrl;
        if(joinRequestDto.getKakoImg()==null){
            imageUploadUrl=imageUpload(joinRequestDto.getProfileImg());
        }
        else{
            imageUploadUrl=joinRequestDto.getKakoImg();
        }

        userRepository.save(new User(joinRequestDto.getNickname(),joinRequestDto.getEmail(),
                imageUploadUrl,"kakao"));
    }

    // 로그인
    @Transactional(rollbackFor = Exception.class)
    public String login(LoginRequestDto requestDto) throws JsonProcessingException {

        String hashedPassword=passwordEncoder.encode(requestDto.getPassword());

        User user=userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(()->{ throw new BaseException(NOT_EXISTS_EMAIL);});

        if(passwordEncoder.matches(user.getPassword(),hashedPassword)){
            throw new BaseException(PASSWORD_NOT_EQUAL);
        }

        redisService.saveUser(user);

        return jwtService.getToken(user);
    }

    //카카오 로그인
    @Transactional(rollbackFor = Exception.class)
    public String kakaoLogin(KakaoLoginRequestDto requestDto,KakaoInfoDto.Account account) throws JsonProcessingException {

        if(account.getEmail()!=requestDto.getEmail()){
            throw new BaseException(KAKAO_LOGIN_FAIL);
        }

        User user=userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(()->{throw new BaseException(NOT_EXISTS_EMAIL);});

        redisService.saveUser(user);

        return jwtService.getToken(user);
    }



    // 번호 조작 함수
    String trimPhonNumber(String phoneNumber){
        return "+82"+phoneNumber.substring(1);
    }

    // 이미지 업로드 함수
    String imageUpload(MultipartFile imageFile) throws BaseException{
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String current_date = simpleDateFormat.format(new Date());

            String basePath = new File("").getAbsolutePath()+fileUploadPath;


            String[] imageFileFlags = imageFile.getOriginalFilename().split("\\.");
            String imageExt=imageFileFlags[imageFileFlags.length-1];

            String imagePath = basePath + "image" + current_date + "." + imageExt;
            String imageDownUrl = fileDownPath + "image" + current_date + "." + imageExt;

            File dest = new File(imagePath);
            imageFile.transferTo(dest);

            return imageDownUrl;
        }
        catch(Exception e){
            throw new BaseException(FILE_UPLOAD_ERROR);
        }
    }

}
