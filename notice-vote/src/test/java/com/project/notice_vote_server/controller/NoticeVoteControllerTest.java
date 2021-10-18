package com.project.notice_vote_server.controller;

import com.google.gson.Gson;
import com.project.common.service.JwtService;
import com.project.notice_vote_server.dto.CreateNoticeRequestDto;
import com.project.notice_vote_server.service.NoticeVoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class NoticeVoteControllerTest {

    @InjectMocks
    private NoticeVoteController noticeVoteController;

    @Mock
    private NoticeVoteService noticeVoteService;

    @Mock
    private JwtService jwtService;

    private MockMvc mockMvc;


    @BeforeEach
    public void init(){
        mockMvc= MockMvcBuilders.standaloneSetup(noticeVoteController).build();
    }

    @Test
    void createTodoTest() throws Exception {

        CreateNoticeRequestDto requestDto=new CreateNoticeRequestDto("aaa");

        final ResultActions resultActions=mockMvc.perform(
                MockMvcRequestBuilders.post("/room/2/notice")
                        .header("access_token","aaa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))

        );

        resultActions.andExpect(status().isOk()).andReturn();


    }
}