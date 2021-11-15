package com.project.common.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.common.error.BaseException;
import com.project.common.model.User;
import com.project.common.response.BaseResponseStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;




@Service
public class JwtService {

    // 토큰 검증후 유저아이디반환
    public Long verifyTokenAndGetUserId(String jwtToken) {
        return JWT.require(Algorithm.HMAC512("jwt sign value")).build()
                .verify(jwtToken).getClaim("id").asLong();
    }

    // 토큰 생성
    public String getToken(User user)  {
        String jwtToken = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 60 * 24 * 30)))
                .withClaim("id", user.getId())
                .withClaim("email", user.getEmail())
                .sign(Algorithm.HMAC512("jwt sign value"));
        return jwtToken;
    }
}
