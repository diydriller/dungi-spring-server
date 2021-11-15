package com.project.api_server.memo.controller;

import com.project.common.aop.BaseExceptionResponseAnnotation;
import com.project.common.response.BaseResponse;
import com.project.common.service.JwtService;
import com.project.api_server.memo.dto.CreateMemoRequestDto;
import com.project.api_server.memo.dto.MoveMemoRequestDto;
import com.project.api_server.memo.dto.UpdateMemoRequestDto;
import com.project.api_server.memo.service.MemoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.project.common.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemoController {
    private final MemoService memoService;
    private final JwtService jwtService;

    // 메모 생성
    @BaseExceptionResponseAnnotation
    @PostMapping("/room/{roomId}/memo")
    BaseResponse createMemo(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token,
            @RequestBody @Valid CreateMemoRequestDto memoRequestDto)throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            memoService.createMemo(memoRequestDto,userId,roomId);
            return new BaseResponse(SUCCESS);
    }

    // 메모 조회
    @BaseExceptionResponseAnnotation
    @GetMapping("/room/{roomId}/memo")
    BaseResponse getMemo(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token)throws Exception{
            Long userId = jwtService.verifyTokenAndGetUserId(token);
            return new BaseResponse(memoService.getMemo(roomId,userId));
    }

    //메모 수정
    @BaseExceptionResponseAnnotation
    @PutMapping("/room/{roomId}/memo/{memoId}/update")
    BaseResponse updateMemo(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long memoId,
            @RequestBody @Valid UpdateMemoRequestDto memoRequestDto)throws Exception {
        Long userId=jwtService.verifyTokenAndGetUserId(token);
        memoService.updateMemo(memoRequestDto,userId,roomId,memoId);
        return new BaseResponse(SUCCESS);
    }

    // 메모 이동
    @BaseExceptionResponseAnnotation
    @PutMapping("/room/{roomId}/memo/{memoId}/move")
    BaseResponse moveMemo(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long memoId,
            @RequestBody @Valid MoveMemoRequestDto memoRequestDto)throws Exception {
        Long userId=jwtService.verifyTokenAndGetUserId(token);
        memoService.moveMemo(memoRequestDto,userId,roomId,memoId);
        return new BaseResponse(SUCCESS);
    }

    // 메모 삭제
    @BaseExceptionResponseAnnotation
    @DeleteMapping("/room/{roomId}/memo/{memoId}")
    BaseResponse deleteMemo(
            @PathVariable Long roomId,
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long memoId,
            @RequestBody @Valid MoveMemoRequestDto memoRequestDto)throws Exception {
        Long userId=jwtService.verifyTokenAndGetUserId(token);
        memoService.deleteMemo(memoRequestDto,userId,roomId,memoId);
        return new BaseResponse(SUCCESS);
    }


}
