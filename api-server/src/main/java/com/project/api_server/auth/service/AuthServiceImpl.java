package com.project.api_server.auth.service;

import com.project.api_server.auth.dto.*;
import com.project.common.cipher.SymmetricCipher;
import com.project.common.error.BaseException;
import com.project.common.file.FileUploader;
import com.project.common.model.User;
import com.project.common.repository.UserRepository;
import com.project.common.service.JwtService;
import com.project.common.service.RedisService;
import com.project.api_server.auth.sms.SmsSender;
import com.project.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.project.common.response.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder hashCipher;
    private final SymmetricCipher symmetricCipher;
    private final RedisService redisService;
    private final JwtService jwtService;
    private final FileUploader fileUploader;
    private final SmsSender smsSender;

    // 이미지 업로드 - 암호화 - 유저생성
    @Transactional
    public void createUser(JoinRequestDto requestDto) throws Exception {
        String imageDownUrl = fileUploader.imageUpload(requestDto.getImg());
        String hashedPassword = hashCipher.encode(requestDto.getPassword());
        User user = requestDto.toEntity(imageDownUrl,hashedPassword);
        symmetricCipher.encryptUser(user);
        userRepository.save(user);
    }

    // 암호화 - 이메일 조회
    public void checkEmailPresent(String email) throws Exception {
        email = symmetricCipher.encrypt(email);
        userRepository.findByEmail(email)
                .ifPresent(m->{throw new BaseException(ALREADY_EXISTS_EMAIL);});
    }

    // 랜덤번호 - 암호화 - 레디스 저장 - sms 전송
    @Transactional(rollbackFor = Exception.class)
    public void sendSms(SendSmsRequestDto requestDto) throws Exception {

        String randomNumber = StringUtil.randomNumber();
        String trimmedPhoneNumber = StringUtil.trimPhoneNumber(requestDto.getPhoneNumber());
        String phoneNumber = symmetricCipher.encrypt(trimmedPhoneNumber);
        redisService.setStringOpsTtl(phoneNumber, randomNumber, 90, TimeUnit.SECONDS);
        smsSender.sendSms(trimmedPhoneNumber,randomNumber);
    }

    // 레디스 조회 - 인증번호 비교
    public void compareCode(CheckCodeRequestDto requestDto) throws Exception {

        String phoneNumber = symmetricCipher.encrypt(StringUtil.trimPhoneNumber(requestDto.getPhoneNumber()));
        String savedCode = redisService
                .getStringOps(phoneNumber)
                .map(Object::toString)
                .orElseThrow(() -> {
                    throw new BaseException(CODE_NOT_EXIST);
                });

        if (savedCode.equals(requestDto.getCode())) {
            throw new BaseException(CODE_NOT_EQUAL);
        }
    }

    // 이미지 업로드 - 암호화 - 유저 생성
    @Transactional
    public void createSnsUser(SnsJoinRequestDto requestDto) throws Exception {
        String imageDownUrl = requestDto.getKakaoImg()==null?
                fileUploader.imageUpload(requestDto.getProfileImg())
                : requestDto.getKakaoImg();

        User user = requestDto.toEntity(imageDownUrl);
        symmetricCipher.encryptUser(user);
        userRepository.save(user);
    }

    // 암호화 - 이메일 조회 - 비밀번호 일치여부 - 레디스 기록 - 토큰 발행
    @Transactional(rollbackFor = Exception.class,readOnly = true)
    public String login(LoginRequestDto requestDto) throws Exception {

        String hashedPassword = hashCipher.encode(requestDto.getPassword());
        String email = symmetricCipher.encrypt(requestDto.getEmail());
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->{ throw new BaseException(NOT_EXISTS_EMAIL);});

        if(hashCipher.matches(symmetricCipher.decrypt(user.getPassword()),hashedPassword)){
            throw new BaseException(PASSWORD_NOT_EQUAL);
        }
        redisService.saveUser(user);
        return jwtService.getToken(user);
    }

    // 암호화 - 이메일 조회 - 레디스 기록 - 토큰 발행
    @Transactional(rollbackFor = Exception.class,readOnly = true)
    public String snsLogin(SnsLoginRequestDto requestDto, String email)
            throws Exception {

        if(!email.equals(requestDto.getEmail())){
            throw new BaseException(KAKAO_LOGIN_FAIL);
        }
        email = symmetricCipher.encrypt(requestDto.getEmail());
        User user=userRepository.findByEmail(email)
                .orElseThrow(()->{throw new BaseException(NOT_EXISTS_EMAIL);});

        redisService.saveUser(user);

        return jwtService.getToken(user);
    }






}
