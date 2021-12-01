package com.project.api_server.infrastructure.sns;

import com.project.api_server.application.user.dto.SnsInfoDto;
import com.project.api_server.application.user.dto.SnsTokenDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface KakaoHttpInterface {
    @GET("/v2/user/me")
    Call<SnsInfoDto> getKakaoInfo(
            @Header("Authorization") String token,
            @Header("content-type") String type
    );


    @POST("/oauth/token")
    Call<SnsTokenDto> getKakaoToken(
            @Query("grant_type") String grantType,
            @Query("client_id") String clientId,
            @Query("redirect_uri") String redirectUri,
            @Query("code") String code,
            @Query("client_secret") String secret,
            @Header("content-type") String type
    );


}
