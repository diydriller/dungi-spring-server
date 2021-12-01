package com.project.api_server.application.memo.controller;

import com.project.common.error.AuthenticationException;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
import com.project.api_server.application.memo.dto.CreateMemoRequestDto;
import com.project.api_server.application.memo.dto.MoveMemoRequestDto;
import com.project.api_server.application.memo.dto.UpdateMemoRequestDto;
import com.project.api_server.domain.memo.service.MemoServiceImpl;
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
public class MemoController {

    private final MemoServiceImpl memoService;

    private String LOGIN_USER="login_user";

    // 메모 생성
    @PostMapping("/room/{roomId}/memo")
    BaseResponse createMemo(
            @PathVariable Long roomId,
            @RequestBody @Valid CreateMemoRequestDto memoRequestDto,HttpSession session) {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
        memoService.createMemo(memoRequestDto,user,roomId);
        return new BaseResponse(SUCCESS);
    }

    // 메모 조회
    @GetMapping("/room/{roomId}/memo")
    BaseResponse getMemo(
            @PathVariable Long roomId,HttpSession session){
        User user = (User) session.getAttribute(LOGIN_USER);
        return new BaseResponse(memoService.getMemo(roomId,user));
    }

    //메모 수정
    @PutMapping("/room/{roomId}/memo/{memoId}/update")
    BaseResponse updateMemo(
            @PathVariable Long roomId,
            @PathVariable Long memoId,
            @RequestBody @Valid UpdateMemoRequestDto memoRequestDto,HttpSession session){
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
        memoService.updateMemo(memoRequestDto,user,roomId,memoId);
        return new BaseResponse(SUCCESS);
    }

    // 메모 이동
    @PutMapping("/room/{roomId}/memo/{memoId}/move")
    BaseResponse moveMemo(
            @PathVariable Long roomId,
            @PathVariable Long memoId,
            @RequestBody @Valid MoveMemoRequestDto memoRequestDto,HttpSession session) {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
        memoService.moveMemo(memoRequestDto,user,roomId,memoId);
        return new BaseResponse(SUCCESS);
    }

    // 메모 삭제
    @DeleteMapping("/room/{roomId}/memo/{memoId}")
    BaseResponse deleteMemo(
            @PathVariable Long roomId,
            @PathVariable Long memoId,HttpSession session) {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
        memoService.deleteMemo(user,roomId,memoId);
        return new BaseResponse(SUCCESS);
    }


}
