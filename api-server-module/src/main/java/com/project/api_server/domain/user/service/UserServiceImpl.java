package com.project.api_server.domain.user.service;

import com.project.api_server.application.user.dto.*;
import com.project.api_server.domain.sns.SnsHttpService;
import com.project.common.infrastructure.cipher.SymmetricCipher;
import com.project.common.error.BaseException;
import com.project.api_server.infrastructure.file.FileUploader;
import com.project.common.model.User;
import com.project.common.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static com.project.common.response.BaseResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder hashCipher;
    private final SymmetricCipher symmetricCipher;
    private final FileUploader fileUploader;
    private final UserStore userStore;
    private final SnsHttpService snsHttpService;


    // 이메일 중복 여부 - 이미지 업로드 - 비밀번호 암호화 - 유저정보 암호화 - DB 저장
    @Transactional
    public void createUser(JoinRequestDto requestDto) throws Exception {
        checkEmailPresent(requestDto.getEmail());
        String imageDownUrl = fileUploader.imageUpload(requestDto.getImg());
        String hashedPassword = hashCipher.encode(requestDto.getPassword());
        User user = requestDto.toEntity(imageDownUrl,hashedPassword);
        symmetricCipher.encryptUser(user);
        userStore.saveUser(user);
    }

    // 이메일 암호화 - 이메일 조회
    public void checkEmailPresent(String email) throws Exception {
        email = symmetricCipher.encrypt(email);
        userStore.checkEmailPresent(email);
    }

    // 랜덤번호 생성 - 암호화 - 레디스 저장 - sms 전송
    @Transactional(rollbackFor = Exception.class)
    public void sendSms(SendSmsRequestDto requestDto) throws Exception {
        String randomNumber = StringUtil.randomNumber();
        String trimmedPhoneNumber = StringUtil.trimPhoneNumber(requestDto.getPhoneNumber());
        String phoneNumber = symmetricCipher.encrypt(trimmedPhoneNumber);
        userStore.saveCode(phoneNumber, randomNumber);
        userStore.sendSms(trimmedPhoneNumber,randomNumber);
    }

    // 레디스 조회 - 인증번호 비교
    public void compareCode(CheckCodeRequestDto requestDto) throws Exception {
        String phoneNumber = symmetricCipher.encrypt(StringUtil.trimPhoneNumber(requestDto.getPhoneNumber()));
        String savedCode = userStore.getCode(phoneNumber);
        if (savedCode.equals(requestDto.getCode())) {
            throw new BaseException(CODE_NOT_EQUAL);
        }
    }

    // 이메일 검증 - 이메일 중복여부 - 이미지 업로드 - 유저정보 암호화 - DB 저장
    @Transactional
    public void createSnsUser(SnsJoinRequestDto requestDto) throws Exception {
        String email = snsHttpService.getSnsInfo(requestDto.getAccess_token());
        if(email != requestDto.getEmail()){
            throw new BaseException(NOT_EXISTS_EMAIL);
        }
        checkEmailPresent(requestDto.getEmail());
        String imageDownUrl = requestDto.getKakaoImg()==null?
                fileUploader.imageUpload(requestDto.getProfileImg())
                : requestDto.getKakaoImg();
        User user = requestDto.toEntity(imageDownUrl);
        symmetricCipher.encryptUser(user);
        userStore.saveUser(user);
    }




    // 암호화 - 이메일 조회 - 비밀번호 일치여부
    @Transactional(readOnly = true)
    public User login(LoginRequestDto requestDto) throws Exception {

        String email = symmetricCipher.encrypt(requestDto.getEmail());
        User user = userStore.findUserByEmail(email);
        if(!hashCipher.matches(requestDto.getPassword(),symmetricCipher.decrypt(user.getPassword()))){
            throw new BaseException(PASSWORD_NOT_EQUAL);
        }
        return user;
    }

    // 이메일 검증 - 이메일 일치여부 - 암호화 - 이메일 조회
    @Transactional(readOnly = true)
    public User snsLogin(SnsLoginRequestDto requestDto)
            throws Exception {
        String email = snsHttpService.getSnsInfo(requestDto.getAccess_token());
        if(!email.equals(requestDto.getEmail())){
            throw new BaseException(KAKAO_LOGIN_FAIL);
        }
        email = symmetricCipher.encrypt(requestDto.getEmail());
        User user = userStore.findUserByEmail(email);
        return user;
    }

    // 카카오 토큰 가져오기
    public SnsTokenDto snsToken(String code) throws Exception {
        return snsHttpService.getSnsToken(code);
    }






}
