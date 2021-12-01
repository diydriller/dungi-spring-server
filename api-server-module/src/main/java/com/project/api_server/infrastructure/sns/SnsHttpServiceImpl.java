package com.project.api_server.infrastructure.sns;

import com.project.api_server.application.user.dto.SnsInfoDto;
import com.project.api_server.application.user.dto.SnsTokenDto;
import com.project.api_server.domain.sns.SnsHttpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Component
public class SnsHttpServiceImpl implements SnsHttpService {

    @Value("${kakao.accountId}")
    private String kakaoAccountId;
    @Value("${kakao.secret}")
    private String kakaoSecret;
    @Value("${kakao.callbackUri}")
    private String kakaoCallbackUri;

    private KakaoHttpInterface kakaoApiService;
    private KakaoHttpInterface kakaoAuthService;

    public SnsHttpServiceImpl() {
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

    // 카카오 이메일 가져오기
    public String getSnsInfo(String token) throws Exception {
        Call<SnsInfoDto> retrofitCall = kakaoApiService.getKakaoInfo("Bearer "+token,"application/x-www-form-urlencoded");
        Response<SnsInfoDto> response = retrofitCall.execute();
        return response.body().getKakao_account().getEmail();
    }

    // 카카오 토큰 가져오기
    public SnsTokenDto getSnsToken(String code) throws Exception{

        Call<SnsTokenDto> retrofitCall = kakaoAuthService.getKakaoToken(
                "authorization_code", kakaoAccountId,kakaoCallbackUri,code,kakaoSecret
                ,"application/x-www-form-urlencoded");
        Response<SnsTokenDto> response = retrofitCall.execute();
        return response.body();
    }



}
