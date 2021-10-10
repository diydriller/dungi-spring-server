package com.project.common.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.common.error.BaseException;
import com.project.common.model.User;
import com.project.common.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.project.common.response.BaseResponseStatus.JWT_ERROR;


@Service
@AllArgsConstructor
public class JwtService {

    // 토큰 검증후 유저아이디반환
    public Long verifyTokenAndGetUserId(String jwtToken) {
        try {
            return JWT.require(Algorithm.HMAC512("jwt sign value")).build()
                    .verify(jwtToken).getClaim("id").asLong();
        }
        catch (BaseException e){
            throw new BaseException(JWT_ERROR);
        }
    }

    // 토큰 생성
    public String getToken(User user)  {
        try {
            String jwtToken = JWT.create()
                    .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 60 * 60 * 24)))
                    .withClaim("id", user.getId())
                    .withClaim("email", user.getEmail())
                    .sign(Algorithm.HMAC512("jwt sign value"));
            return jwtToken;
        }
        catch (BaseException e){
            throw new BaseException(JWT_ERROR);
        }
    }
}
