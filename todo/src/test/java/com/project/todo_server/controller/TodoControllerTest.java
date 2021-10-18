package com.project.todo_server.controller;

import com.google.gson.Gson;
import com.project.todo_server.dto.CreateTodayTodoRequestDto;
import com.project.todo_server.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {


    @InjectMocks
    private TodoController todoController;

    @Mock
    private TodoService todoService;

    private MockMvc mockMvc;


    @BeforeEach
    public void init(){
        mockMvc= MockMvcBuilders.standaloneSetup(todoController).build();
    }

    @Test
    void createTodoTest() throws Exception {

        CreateTodayTodoRequestDto requestDto=new CreateTodayTodoRequestDto("test","2021/11/11/10/30");

        final ResultActions resultActions=mockMvc.perform(
                MockMvcRequestBuilders.post("/room/2/todo/day")
                        .header("access_token","aaa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))

        );

        resultActions.andExpect(status().isOk()).andReturn();


    }

}