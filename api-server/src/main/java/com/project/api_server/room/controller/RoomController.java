package com.project.api_server.room.controller;

import com.project.common.aop.BaseExceptionResponseAnnotation;
import com.project.common.response.BaseResponse;
import com.project.common.service.JwtService;
import com.project.api_server.room.dto.CreateRoomRequestDto;
import com.project.api_server.room.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.project.common.response.BaseResponseStatus.SUCCESS;

@RestController
@AllArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;
    private final JwtService jwtService;

    // 방 생성
    @BaseExceptionResponseAnnotation
    @PostMapping("/room")
    BaseResponse createRoom(@RequestBody @Valid CreateRoomRequestDto roomRequestDto,
                            @RequestHeader(value = "access_token") String token
    ) throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            roomService.createRoom(roomRequestDto,userId);
            return new BaseResponse(SUCCESS);
    }

    // 방 입장
    @BaseExceptionResponseAnnotation
    @PostMapping("/room/{roomId}/member")
    BaseResponse enterRoom(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId
    ) throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            roomService.enterRoom(userId,roomId);
            return new BaseResponse(SUCCESS);
    }


    // 방 퇴장 , 방 삭제
    @BaseExceptionResponseAnnotation
    @DeleteMapping("/room/{roomId}/member")
    BaseResponse leaveRoom(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId
    ) throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            roomService.leaveRoom(userId,roomId);
            return new BaseResponse(SUCCESS);
    }

    // 방 조회
    @BaseExceptionResponseAnnotation
    @GetMapping("/room")
    BaseResponse<?> getRoom(
            @RequestHeader(value = "access_token") String token,
            @RequestParam("page") int page
    ) throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            return new BaseResponse(roomService.getRoom(userId,page));
    }

}
