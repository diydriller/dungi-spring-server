package com.project.room_server.controller;

import com.google.gson.Gson;
import com.project.common.service.JwtService;
import com.project.room_server.dto.CreateRoomRequestDto;
import com.project.room_server.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RoomControllerTest {

    @InjectMocks
    private RoomController roomController;
    @Mock
    private RoomService roomService;
    @Mock
    private JwtService jwtService;

    private MockMvc mockMvc;


    @BeforeEach
    public void init(){
        mockMvc= MockMvcBuilders.standaloneSetup(roomController).build();
    }

    @Test
    void createRoomTest() throws Exception {

        CreateRoomRequestDto requestDto=new CreateRoomRequestDto("aaa","aaa");

        final ResultActions resultActions=mockMvc.perform(
                MockMvcRequestBuilders.post("/room")
                        .header("access_token","aaa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))

        );

        resultActions.andExpect(status().isOk()).andReturn();
    }


}