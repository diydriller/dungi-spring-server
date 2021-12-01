package com.project.api_server.application.room.controller;


import com.project.common.error.AuthenticationException;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
import com.project.api_server.application.room.dto.CreateRoomRequestDto;
import com.project.api_server.domain.room.service.RoomServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Optional;

import static com.project.common.response.BaseResponseStatus.AUTHENTICATION_ERROR;
import static com.project.common.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomServiceImpl roomService;

    private String LOGIN_USER="login_user";

    // 방 생성
    @PostMapping("/room")
    BaseResponse createRoom(@RequestBody @Valid CreateRoomRequestDto roomRequestDto, HttpSession session)
            throws Exception {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
        roomService.createRoom(roomRequestDto,user);
        return new BaseResponse(SUCCESS);
    }

    // 방 입장
    @PostMapping("/room/{roomId}/member")
    BaseResponse enterRoom(
            @PathVariable Long roomId,HttpSession session
    ) throws Exception {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
        roomService.enterRoom(user,roomId);
        return new BaseResponse(SUCCESS);
    }


    // 방 퇴장 , 방 삭제
    @DeleteMapping("/room/{roomId}/member")
    BaseResponse leaveRoom(
            @PathVariable Long roomId,HttpSession session
    ) throws Exception {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
            roomService.leaveRoom(user,roomId);
            return new BaseResponse(SUCCESS);
    }

    // 방 조회
    @GetMapping("/room")
    BaseResponse<?> getRoom(
            @RequestParam("page") int page,HttpSession session
    ) throws Exception {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
            return new BaseResponse(roomService.getRoom(user,page));
    }

}
