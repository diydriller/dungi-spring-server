package com.project.auth_server.controller;

import com.google.gson.Gson;
import com.project.auth_server.dto.JoinRequestDto;
import com.project.auth_server.dto.LoginRequestDto;
import com.project.auth_server.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    private MockMvc mockMvc;


    @BeforeEach
    public void init(){
        mockMvc= MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void loginTest() throws Exception {
        LoginRequestDto requestDto=new LoginRequestDto("aaa@naver.com","aaa");

        final ResultActions resultActions=mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
        );

        resultActions.andExpect(status().isOk()).andReturn();


    }

}