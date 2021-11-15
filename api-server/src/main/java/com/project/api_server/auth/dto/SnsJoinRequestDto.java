package com.project.api_server.auth.dto;

import com.project.common.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class SnsJoinRequestDto {

    @NotEmpty(message = "email is empty")
    @Pattern(regexp = "\\w+@\\w+.\\w+",message = "email format is wrong")
    private String email;

    @NotEmpty(message = "nickname is empty")
    @Size(max=10,message = "nickname's max length is 10")
    private String nickname;

    private String kakaoImg;

    @NotEmpty(message = "token is empty")
    private String access_token;

    private MultipartFile profileImg;

    public User toEntity(String imageDownUrl){
        return User.builder()
                .nickname(nickname)
                .email(email)
                .profileImg(imageDownUrl)
                .provider("kakao")
                .build();
    }

}
