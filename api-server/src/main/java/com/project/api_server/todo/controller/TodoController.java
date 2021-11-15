package com.project.api_server.todo.controller;

import com.project.common.aop.BaseExceptionResponseAnnotation;
import com.project.common.response.BaseResponse;
import com.project.common.service.JwtService;
import com.project.api_server.todo.dto.CreateRepeatTodoRequestDto;
import com.project.api_server.todo.dto.CreateTodayTodoRequestDto;
import com.project.api_server.todo.service.TodoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.project.common.response.BaseResponseStatus.SUCCESS;

@RestController
@AllArgsConstructor
@Slf4j
public class TodoController {

    private final TodoService todoService;
    private final JwtService jwtService;

    // 하루 할일 생성
    @BaseExceptionResponseAnnotation
    @PostMapping("/room/{roomId}/todo/day")
    BaseResponse createTodayTodo(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId,
            @RequestBody @Valid CreateTodayTodoRequestDto todoRequestDto
    ) throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            todoService.createTodayTodo(todoRequestDto,userId,roomId);
            return new BaseResponse(SUCCESS);
    }

    // 반복 할일 생성
    @BaseExceptionResponseAnnotation
    @PostMapping("/room/{roomId}/todo/days")
    BaseResponse createRepeatTodo(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId,
            @RequestBody @Valid CreateRepeatTodoRequestDto todoRequestDto
    ) throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            todoService.createRepeatTodo(todoRequestDto,userId,roomId);
            return new BaseResponse(SUCCESS);
    }


    // 하루 할일 조회
    @BaseExceptionResponseAnnotation
    @GetMapping("/room/{roomId}/todo/day")
    BaseResponse getTodayTodo(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId,
            @RequestParam("page") int page
    ) throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            return new BaseResponse(todoService.getTodayTodo(userId,roomId,page));
    }

    // 반복할일 조회
    @BaseExceptionResponseAnnotation
    @GetMapping("/room/{roomId}/todo/days")
    BaseResponse getRepeatTodo(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId,
            @RequestParam("page") int page
    ) throws Exception {
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            return new BaseResponse(todoService.getRepeatTodo(userId,roomId,page));
    }


}
