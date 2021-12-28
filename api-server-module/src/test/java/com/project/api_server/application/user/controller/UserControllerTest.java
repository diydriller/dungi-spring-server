package com.project.api_server.application.user.controller;

import com.google.gson.Gson;
import com.project.api_server.application.user.dto.JoinRequestDto;
import com.project.api_server.domain.user.service.UserService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc= MockMvcBuilders.standaloneSetup(userController).build();
    }

    @DisplayName("회원가입 성공")
    @Test
    void signup_test() throws Exception {

        JoinRequestDto requestDto = JoinRequestDto.builder()
                .email("aaa@naver.com")
                .password("aaa")
                .nickname("park")
                .phoneNumber("01012341234")
                .img(any(MultipartFile.class))
                .name("park")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/user").
                contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(requestDto))
        )
                .andExpect(status().isOk());
    }

}