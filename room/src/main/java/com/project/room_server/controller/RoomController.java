package com.project.room_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.common.error.BaseException;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
import com.project.common.service.JwtService;
import com.project.common.service.RedisService;
import com.project.room_server.dto.CreateRoomRequestDto;
import com.project.room_server.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import static com.project.common.response.BaseResponseStatus.*;

@RestController
@AllArgsConstructor
@Slf4j
public class RoomController {

    private final RoomService roomService;
    private final JwtService jwtService;

    // 방 생성
    @PostMapping("/room")
    BaseResponse createRoom(@RequestBody @Valid CreateRoomRequestDto roomRequestDto,
                            @RequestHeader(value = "access_token") String token
    )throws BaseException {
        try{
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            roomService.createRoom(roomRequestDto,userId);
            return new BaseResponse(SUCCESS);
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

    // 방 입장
    @PostMapping("/room/{roomId}/member")
    BaseResponse enterRoom(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId
    )throws BaseException{
        try{
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            roomService.enterRoom(userId,roomId);
            return new BaseResponse(SUCCESS);
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }


    // 방 퇴장 , 방 삭제
    @DeleteMapping("/room/{roomId}/member")
    BaseResponse leaveRoom(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId
    )throws BaseException{
        try{
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            roomService.leaveRoom(userId,roomId);
            return new BaseResponse(SUCCESS);
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

    // 방 조회
    @GetMapping("/room")
    BaseResponse<?> getRoom(
            @RequestHeader(value = "access_token") String token,
            @RequestParam("page") int page
    )throws BaseException{
        try{
            Long userId=jwtService.verifyTokenAndGetUserId(token);

            return new BaseResponse(roomService.getRoom(userId,page));
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

}
