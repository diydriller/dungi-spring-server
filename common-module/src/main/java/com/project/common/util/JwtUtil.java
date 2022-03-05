package com.project.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.common.model.User;


import java.util.Date;

public class JwtUtil {

    private static String JWT_CLAIM_FIELD_1="id";
    private static String JWT_CLAIM_FIELD_2="email";
    private static String JWT_SECRET="jwt sign value";

    // 토큰 검증
    public static void verifyToken(String jwtToken) {
        JWT.require(Algorithm.HMAC512(JWT_SECRET)).build().verify(jwtToken);
    }

    // 토큰 생성
    public static String getToken(User user)  {
        String jwtToken = JWT.create()
                .withClaim(JWT_CLAIM_FIELD_1, user.getId())
                .withClaim(JWT_CLAIM_FIELD_2, user.getEmail())
                .sign(Algorithm.HMAC512(JWT_SECRET));
        return jwtToken;
    }
}
