package com.project.auth_server.service;

import com.project.auth_server.dto.KakaoTokenDto;
import com.project.auth_server.http.KakaoHttpInterface;
import com.project.auth_server.dto.KakaoInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Service
public class KakaoHttpService {

    @Value("${kakao.accountId}")
    private String kakaoAccountId;
    @Value("${kakao.secret}")
    private String kakaoSecret;
    @Value("${kakao.callbackUri}")
    private String kakaoCallbackUri;

    private KakaoHttpInterface kakaoApiService;
    private KakaoHttpInterface kakaoAuthService;

    public KakaoHttpService() {
        Retrofit KakaoApiRetrofit=new Retrofit.Builder()
                .baseUrl("https://kapi.kakao.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        kakaoApiService=KakaoApiRetrofit.create(KakaoHttpInterface.class);

        Retrofit KakaoAuthRetrofit=new Retrofit.Builder()
                .baseUrl("https://kauth.kakao.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        kakaoAuthService=KakaoAuthRetrofit.create(KakaoHttpInterface.class);

    }

    // 카카오 정보 가져오기
    public KakaoInfoDto getKakaoInfo(String token) throws IOException {
        Call<KakaoInfoDto> retrofitCall = kakaoApiService.getKakaoInfo("Bearer "+token,"application/x-www-form-urlencoded");
        Response<KakaoInfoDto> response = retrofitCall.execute();
        return response.body();
    }

    // 카카오 토큰 가져오기
    public KakaoTokenDto getKakaoToken(String code) throws IOException{

        Call<KakaoTokenDto> retrofitCall = kakaoAuthService.getKakaoToken(
                "authorization_code", kakaoAccountId,kakaoCallbackUri,code,kakaoSecret
                ,"application/x-www-form-urlencoded");
        Response<KakaoTokenDto> response = retrofitCall.execute();
        System.out.println(response.raw());
        return response.body();
    }



}
