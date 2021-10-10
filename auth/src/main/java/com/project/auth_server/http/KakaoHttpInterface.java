package com.project.auth_server.http;

import com.project.auth_server.dto.KakaoInfoDto;
import com.project.auth_server.dto.KakaoTokenDto;
import retrofit2.Call;
import retrofit2.http.*;

public interface KakaoHttpInterface {
    @GET("/v2/user/me")
    Call<KakaoInfoDto> getKakaoInfo(
            @Header("Authorization") String token,
            @Header("content-type") String type
    );


    @POST("/oauth/token")
    Call<KakaoTokenDto> getKakaoToken(
            @Query("grant_type") String grantType,
            @Query("client_id") String clientId,
            @Query("redirect_uri") String redirectUri,
            @Query("code") String code,
            @Query("client_secret") String secret,
            @Header("content-type") String type
    );


}
