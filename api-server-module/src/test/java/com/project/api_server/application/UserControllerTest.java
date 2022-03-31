package com.project.api_server.application;

import com.google.gson.Gson;
import com.project.api_server.application.user.controller.UserController;
import com.project.api_server.application.user.dto.*;
import com.project.api_server.domain.user.service.UserService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
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

    @DisplayName("1. 회원가입 테스트")
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

    private LoginRequestDto loginRequestDto() {
        LoginRequestDto loginRequestDto=new LoginRequestDto();
        loginRequestDto.setEmail("aaa@naver.com");
        loginRequestDto.setPassword("aaa");
        return loginRequestDto;
    }


    @DisplayName("2. 로그인 테스트")
    @Test
    void sign_in_test() throws Exception {

        // given
        final LoginRequestDto requestDto = loginRequestDto();
        when(userService.login(requestDto)).thenReturn(new User());

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
        );

        //then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        final String token = mvcResult.getResponse().getContentAsString();
        assertThat(token).isNotNull();
    }


    private SendSmsRequestDto sendSmsRequestDto() {
        SendSmsRequestDto requestDto = new SendSmsRequestDto();
        requestDto.setPhoneNumber("01012341234");
        return requestDto;
    }

    @DisplayName("3. sms 전송 테스트")
    @Test
    void send_sms_test() throws Exception {

        // given
        final SendSmsRequestDto requestDto = sendSmsRequestDto() ;
        Mockito.lenient().doNothing().when(userService).sendSms(requestDto);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
        );

        //then
        resultActions.andExpect(status().isOk()).andReturn();
    }


    private CheckCodeRequestDto checkCodeRequestDto() {
        CheckCodeRequestDto requestDto = new CheckCodeRequestDto();
        requestDto.setPhoneNumber("01012341234");
        requestDto.setCode("1234");
        return requestDto;
    }

    @DisplayName("4. sms 인증번호 테스트")
    @Test
    void check_code_test() throws Exception {

        // given
        final CheckCodeRequestDto requestDto = checkCodeRequestDto();
        Mockito.lenient().doNothing().when(userService).compareCode(requestDto);

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/check/phone")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
        );

        //then
        resultActions.andExpect(status().isOk()).andReturn();
    }

    private SnsJoinRequestDto snsJoinRequestDto() {
        SnsJoinRequestDto requestDto = new SnsJoinRequestDto();
        requestDto.setKakaoImg("https://img");
        requestDto.setEmail("aaa@kakao.com");
        requestDto.setNickname("aaa");
        requestDto.setAccess_token("aaa");
        return requestDto;
    }

    @DisplayName("5. 카카오 회원가입 테스트")
    @Test
    void kakao_sign_up_test() throws Exception {

        // given
        final SnsJoinRequestDto requestDto = snsJoinRequestDto();
        Mockito.lenient().doNothing().when(userService).createSnsUser(requestDto);


        // when
        final ResultActions resultActions = mockMvc.perform(multipart("/kakao/user")
                .param("email", requestDto.getEmail())
                .param("nickname", requestDto.getNickname())
                .param("kakaoImg",requestDto.getKakaoImg())
                .param("access_token",requestDto.getAccess_token())
                .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        //then
        resultActions.andExpect(status().isOk()).andReturn();
    }


    private SnsLoginRequestDto snsLoginRequestDto() {
        SnsLoginRequestDto requestDto = new SnsLoginRequestDto();
        requestDto.setEmail("aaa@kakao.com");
        requestDto.setAccess_token("aaa");
        return requestDto;
    }

    @DisplayName("6. 카카오 로그인 테스트")
    @Test
    void kakao_sign_in_test() throws Exception {

        // given
        final SnsLoginRequestDto requestDto = snsLoginRequestDto();
        when(userService.snsLogin(requestDto)).thenReturn(new User());

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/kakao/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(requestDto))
        );

        //then
        final MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();
        final String token = mvcResult.getResponse().getContentAsString();
        assertThat(token).isNotNull();
    }



}