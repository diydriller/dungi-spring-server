package com.project.api_server.application;

import com.google.gson.Gson;
import com.project.api_server.application.memo.controller.MemoController;
import com.project.api_server.application.memo.dto.CreateMemoRequestDto;
import com.project.api_server.application.memo.dto.GetMemoResponseDto;
import com.project.api_server.application.memo.dto.UpdateMemoRequestDto;
import com.project.api_server.domain.memo.service.MemoService;
import com.project.common.model.User;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemoControllerTest {
    @InjectMocks
    private MemoController memoController;
    @Mock
    private MemoService memoService;
    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc= MockMvcBuilders.standaloneSetup(memoController).build();
    }


    private CreateMemoRequestDto createMemoRequestDto() {
        CreateMemoRequestDto requestDto = new CreateMemoRequestDto();
        requestDto.setMemo("test");
        requestDto.setMemoColor("#ffffff");
        requestDto.setX(1.0);
        requestDto.setY(1.0);
        return requestDto;
    }

    private String LOGIN_USER="login_user";

    @DisplayName("1. 메모 생성 테스트")
    @Test
    void create_memo_test() throws Exception {

        // given
        MockHttpSession session=new MockHttpSession();
        session.setAttribute(LOGIN_USER,new User());

        final CreateMemoRequestDto requestDto = createMemoRequestDto();
        Mockito.lenient().doNothing().when(memoService).createMemo(requestDto,new User(),1L);


        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/room/1/memo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
                        .session(session)
        );

        //then
        resultActions.andExpect(status().isOk()).andReturn();
    }


    @DisplayName("2. 메모 조회 테스트")
    @Test
    void get_memo_test() throws Exception {

        // given
        User user=new User();
        MockHttpSession session=new MockHttpSession();
        session.setAttribute(LOGIN_USER,user);

        List<GetMemoResponseDto> memoResponseDtoList=new ArrayList<>();
        doReturn(memoResponseDtoList).when(memoService).getMemo(1L,user);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get("/room/1/memo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
        );

        //then
        resultActions.andExpect(status().isOk()).andReturn();
    }



    private UpdateMemoRequestDto updateMemoRequestDto() {
        UpdateMemoRequestDto requestDto=new UpdateMemoRequestDto();
        requestDto.setMemo("test");
        requestDto.setMemoColor("#ffffff");
        return requestDto;
    }

    @DisplayName("3. 메모 수정 테스트")
    @Test
    void update_memo_test() throws Exception {

        // given
        User user=new User();
        MockHttpSession session=new MockHttpSession();
        session.setAttribute(LOGIN_USER,user);

        UpdateMemoRequestDto requestDto =updateMemoRequestDto();
        Mockito.lenient().doNothing().when(memoService).updateMemo(requestDto,user,1L,1L);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.put("/room/1/memo/1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
                        .session(session)
        );

        //then
        resultActions.andExpect(status().isOk()).andReturn();
    }



}