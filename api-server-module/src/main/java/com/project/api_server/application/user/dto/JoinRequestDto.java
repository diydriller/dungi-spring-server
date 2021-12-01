package com.project.api_server.application.user.dto;

import com.project.common.model.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class JoinRequestDto {

    @NotEmpty(message = "email is empty")
    @Pattern(regexp = "\\w+@\\w+.\\w+",message = "email format is wrong")
    private String email;

    @NotEmpty(message = "password is empty")
    @Size(max=10,message = "password's max length is 10")
    private String password;

    @NotEmpty(message="name is empty")
    @Size(max=10,message = "name's max length is 10")
    private String name;

    @NotEmpty(message = "phoneNumber is empty")
    @Pattern(regexp ="\\d{11}",message = "phoneNumber format is wrong")
    private String phoneNumber;

    @NotEmpty(message = "nickname is empty")
    @Size(max=10,message = "nickname's max length is 10")
    private String nickname;

    @NotNull(message="img is empty")
    private MultipartFile img;

    public User toEntity(String hashedPassword,String imageDownUrl){
        return User.builder()
                .email(email)
                .password(hashedPassword)
                .name(name)
                .phoneNumber(phoneNumber)
                .nickname(nickname)
                .profileImg(imageDownUrl)
                .provider("local")
                .build();
    }
}


