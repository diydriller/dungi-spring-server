package com.project.memo_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.error.BaseException;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
import com.project.common.service.JwtService;
import com.project.common.service.RedisService;
import com.project.memo_server.dto.CreateMemoRequestDto;
import com.project.memo_server.service.MemoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.project.common.response.BaseResponseStatus.JSON_OBJECT_MAPPING_ERROR;
import static com.project.common.response.BaseResponseStatus.SUCCESS;

@RestController
@AllArgsConstructor
@Slf4j
public class MemoController {
    private MemoService memoService;
    private JwtService jwtService;

    // 메모 생성
    @PostMapping("/room/{roomId}/memo")
    BaseResponse createMemo(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token,
            @RequestBody @Valid CreateMemoRequestDto memoRequestDto
            )throws BaseException{
        try{
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            memoService.createMemo(memoRequestDto,userId,roomId);
            return new BaseResponse(SUCCESS);
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

    // 메모 조회
    @GetMapping("/room/{roomId}/memo")
    BaseResponse getMemo(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token
    )throws BaseException{
        try{
            Long userId = jwtService.verifyTokenAndGetUserId(token);
            return new BaseResponse(memoService.getMemo(roomId,userId));
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }
}
