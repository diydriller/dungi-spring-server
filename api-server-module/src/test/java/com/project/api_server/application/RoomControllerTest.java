package com.project.api_server.application;

import com.google.gson.Gson;
import com.project.api_server.application.room.controller.RoomController;
import com.project.api_server.application.room.dto.CreateRoomRequestDto;
import com.project.api_server.application.user.dto.LoginRequestDto;
import com.project.api_server.domain.room.service.RoomService;
import com.project.common.error.AuthenticationException;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

import static com.project.common.response.BaseResponseStatus.AUTHENTICATION_ERROR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RoomControllerTest {

    @InjectMocks
    private RoomController roomController;
    @Mock
    private RoomService roomService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init(){
        mockMvc= MockMvcBuilders.standaloneSetup(roomController).build();
    }


    private CreateRoomRequestDto createRoomRequestDto() {
        CreateRoomRequestDto requestDto = new CreateRoomRequestDto();
        requestDto.setColor("#ffffff");
        requestDto.setName("test");
        return requestDto;
    }

    private String LOGIN_USER="login_user";

    @DisplayName("1. 방 생성 테스트")
    @Test
    void create_room_test() throws Exception {

        // given
        final CreateRoomRequestDto requestDto = createRoomRequestDto();
        User user=new User();
        MockHttpSession session=new MockHttpSession();
        session.setAttribute(LOGIN_USER,user);
        Mockito.lenient().doNothing().when(roomService).createRoom(requestDto,user);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/room")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
                        .session(session)
        );

        //then
        resultActions.andExpect(status().isOk()).andReturn();
    }


    @DisplayName("2. 방 입장 테스트")
    @Test
    void enter_room_test() throws Exception {

        // given
        User user=new User();
        MockHttpSession session=new MockHttpSession();
        session.setAttribute(LOGIN_USER,user);
        Mockito.lenient().doNothing().when(roomService).enterRoom(user,1L);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/room/1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
        );

        //then
        resultActions.andExpect(status().isOk()).andReturn();
    }
}
