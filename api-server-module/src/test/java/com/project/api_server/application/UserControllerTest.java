package com.project.api_server.application;

import com.project.api_server.application.user.controller.UserController;
import com.project.api_server.application.user.dto.JoinRequestDto;
import com.project.api_server.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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

    private JoinRequestDto joinRequestDto(){
        JoinRequestDto joinRequestDto=new JoinRequestDto();
        joinRequestDto.setEmail("aaa@naver.com");
        joinRequestDto.setPassword("aaa");
        joinRequestDto.setPhoneNumber("01012341234");
        MockMultipartFile file = new MockMultipartFile("img", "test.jpg", "image/jpg", "test".getBytes());
        joinRequestDto.setImg(file);
        joinRequestDto.setName("park");
        joinRequestDto.setNickname("park");
        return joinRequestDto;
    }

    @DisplayName("회원가입 테스트")
    @Test
    void sign_up_test() throws Exception {

        // given
        final JoinRequestDto requestDto = joinRequestDto();
        Mockito.lenient().doNothing().when(userService).checkEmailPresent(requestDto.getEmail());
        Mockito.lenient().doNothing().when(userService).createUser(requestDto);

        // when
        final ResultActions resultActions = mockMvc.perform(multipart("/user")
                .file((MockMultipartFile) requestDto.getImg())
                .param("email", requestDto.getEmail())
                .param("password",requestDto.getPassword())
                .param("nickname", requestDto.getNickname())
                .param("name",requestDto.getName())
                .param("phoneNumber",requestDto.getPhoneNumber())
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        //then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        final String token = mvcResult.getResponse().getContentAsString();
        assertThat(token).isNotNull();
    }




}