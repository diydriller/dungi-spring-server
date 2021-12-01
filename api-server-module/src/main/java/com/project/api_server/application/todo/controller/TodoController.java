package com.project.api_server.application.todo.controller;

import com.project.common.error.AuthenticationException;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
import com.project.api_server.application.todo.dto.CreateRepeatTodoRequestDto;
import com.project.api_server.application.todo.dto.CreateTodayTodoRequestDto;
import com.project.api_server.domain.todo.service.TodoServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.util.Optional;

import static com.project.common.response.BaseResponseStatus.AUTHENTICATION_ERROR;
import static com.project.common.response.BaseResponseStatus.SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoServiceImpl todoService;
    private String LOGIN_USER="login_user";

    // 하루 할일 생성
    @PostMapping("/room/{roomId}/todo/day")
    BaseResponse createTodayTodo(
            @PathVariable Long roomId,
            @RequestBody @Valid CreateTodayTodoRequestDto todoRequestDto, HttpSession session
            ) throws Exception {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
            todoService.createTodayTodo(todoRequestDto,user,roomId);
            return new BaseResponse(SUCCESS);
    }

    // 반복 할일 생성
    @PostMapping("/room/{roomId}/todo/days")
    BaseResponse createRepeatTodo(
            @PathVariable Long roomId,
            @RequestBody @Valid CreateRepeatTodoRequestDto todoRequestDto,HttpSession session
    ) throws Exception {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
            todoService.createRepeatTodo(todoRequestDto,user,roomId);
            return new BaseResponse(SUCCESS);
    }


    // 하루 할일 조회
    @GetMapping("/room/{roomId}/todo/day")
    BaseResponse getTodayTodo(
            @PathVariable Long roomId,
            @RequestParam("page") int page,HttpSession session
    ) throws Exception {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
            return new BaseResponse(todoService.getTodayTodo(user,roomId,page));
    }

    // 반복할일 조회
    @GetMapping("/room/{roomId}/todo/days")
    BaseResponse getRepeatTodo(
            @PathVariable Long roomId,
            @RequestParam("page") int page,HttpSession session
    ) throws Exception {
        User user = Optional.ofNullable(session.getAttribute(LOGIN_USER))
                .map(o->(User)o).orElseThrow(()->new AuthenticationException(AUTHENTICATION_ERROR));
            return new BaseResponse(todoService.getRepeatTodo(user,roomId,page));
    }


}
