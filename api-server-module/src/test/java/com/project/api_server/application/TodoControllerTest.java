package com.project.api_server.application;

import com.google.gson.Gson;
import com.project.api_server.application.todo.controller.TodoController;
import com.project.api_server.application.todo.dto.CreateRepeatTodoRequestDto;
import com.project.api_server.application.todo.dto.CreateTodayTodoRequestDto;
import com.project.api_server.application.todo.dto.GetRepeatTodoResponseDto;
import com.project.api_server.application.todo.dto.GetTodayTodoResponseDto;
import com.project.api_server.application.user.controller.UserController;
import com.project.api_server.application.user.dto.JoinRequestDto;
import com.project.api_server.domain.todo.service.TodoService;
import com.project.api_server.domain.user.service.UserService;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TodoControllerTest {

    @InjectMocks
    private TodoController todoController;
    @Mock
    private TodoService todoService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc= MockMvcBuilders.standaloneSetup(todoController).build();
    }


    private CreateTodayTodoRequestDto createTodayTodoRequestDto() {
        CreateTodayTodoRequestDto requestDto = new CreateTodayTodoRequestDto();
        requestDto.setTodo("test");
        requestDto.setTime("2022/03/10/10/10");
        return requestDto;
    }

    private String LOGIN_USER="login_user";

    @DisplayName("1. 하루 할일 생성 테스트")
    @Test
    void create_today_todo_test() throws Exception {

        // given
        final CreateTodayTodoRequestDto requestDto = createTodayTodoRequestDto();
        User user=new User();
        MockHttpSession session=new MockHttpSession();
        session.setAttribute(LOGIN_USER,user);
        Mockito.lenient().doNothing().when(todoService).createTodayTodo(requestDto,user,1L);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/room/1/todo/day")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
                        .session(session)
        );

        //then
        resultActions.andExpect(status().isOk()).andReturn();
    }



    private CreateRepeatTodoRequestDto createRepeatTodoRequestDto() {
        CreateRepeatTodoRequestDto requestDto = new CreateRepeatTodoRequestDto();
        requestDto.setTodo("test");
        requestDto.setDays("1110000");
        requestDto.setTime("15/10");
        return requestDto;
    }

    @DisplayName("2. 반복 할일 생성 테스트")
    @Test
    void create_repeat_todo_test() throws Exception {

        // given
        final CreateRepeatTodoRequestDto requestDto = createRepeatTodoRequestDto();
        User user=new User();
        MockHttpSession session=new MockHttpSession();
        session.setAttribute(LOGIN_USER,user);
        Mockito.lenient().doNothing().when(todoService).createRepeatTodo(requestDto,user,1L);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/room/1/todo/days")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
                        .session(session)
        );

        //then
        resultActions.andExpect(status().isOk()).andReturn();
    }

    @DisplayName("3. 하루 할일 조회 테스트")
    @Test
    void get_today_todo_test() throws Exception {

        // given
        User user=new User();
        MockHttpSession session=new MockHttpSession();
        session.setAttribute(LOGIN_USER,user);
        List<GetTodayTodoResponseDto> todayTodoList=new ArrayList<>();
        when(todoService.getTodayTodo(user,1L,5)).thenReturn(todayTodoList);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/room/1/todo/day?page=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
        );

        //then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        final String responseBody = mvcResult.getResponse().getContentAsString();
        BaseResponse responseDto = new Gson().fromJson(responseBody, BaseResponse.class);
        assertThat(responseDto.getData()).isEqualTo(todayTodoList);
    }

    @DisplayName("4. 반복 할일 조회 테스트")
    @Test
    void get_repeat_todo_test() throws Exception {

        // given
        User user=new User();
        MockHttpSession session=new MockHttpSession();
        session.setAttribute(LOGIN_USER,user);
        List<GetRepeatTodoResponseDto> repeatTodoList=new ArrayList<>();
        when(todoService.getRepeatTodo(user,1L,5)).thenReturn(repeatTodoList);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/room/1/todo/days?page=5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
        );

        //then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        final String responseBody = mvcResult.getResponse().getContentAsString();
        BaseResponse responseDto = new Gson().fromJson(responseBody, BaseResponse.class);
        assertThat(responseDto.getData()).isEqualTo(repeatTodoList);
    }



}
