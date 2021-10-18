package com.project.todo_server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.common.error.BaseException;
import com.project.common.model.User;
import com.project.common.response.BaseResponse;
import com.project.common.service.JwtService;
import com.project.common.service.RedisService;
import com.project.todo_server.dto.CreateRepeatTodoRequestDto;
import com.project.todo_server.dto.CreateTodayTodoRequestDto;
import com.project.todo_server.service.TodoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.project.common.response.BaseResponseStatus.JSON_OBJECT_MAPPING_ERROR;
import static com.project.common.response.BaseResponseStatus.SUCCESS;

@RestController
@AllArgsConstructor
@Slf4j
public class TodoController {

    private final TodoService todoService;
    private final JwtService jwtService;

    // 하루 할일 생성
    @PostMapping("/room/{roomId}/todo/day")
    BaseResponse createTodayTodo(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId,
            @RequestBody @Valid CreateTodayTodoRequestDto todoRequestDto
    )throws BaseException {
        try{
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            todoService.createTodayTodo(todoRequestDto,userId,roomId);
            return new BaseResponse(SUCCESS);
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

    // 반복 할일 생성
    @PostMapping("/room/{roomId}/todo/days")
    BaseResponse createRepeatTodo(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId,
            @RequestBody @Valid CreateRepeatTodoRequestDto todoRequestDto
    )throws BaseException {
        try{
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            todoService.createRepeatTodo(todoRequestDto,userId,roomId);
            return new BaseResponse(SUCCESS);
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }


    // 하루 할일 조회
    @GetMapping("/room/{roomId}/todo/day")
    BaseResponse getTodayTodo(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId,
            @RequestParam("page") int page
    )throws BaseException {
        try{
            Long userId=jwtService.verifyTokenAndGetUserId(token);

            return new BaseResponse(todoService.getTodayTodo(userId,roomId,page));
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }

    // 반복할일 조회
    @GetMapping("/room/{roomId}/todo/days")
    BaseResponse getRepeatTodo(
            @RequestHeader(value = "access_token") String token,
            @PathVariable Long roomId,
            @RequestParam("page") int page
    )throws BaseException {
        try{
            Long userId=jwtService.verifyTokenAndGetUserId(token);
            return new BaseResponse(todoService.getRepeatTodo(userId,roomId,page));
        }
        catch (JsonProcessingException e) {
            return new BaseResponse(JSON_OBJECT_MAPPING_ERROR);
        }
        catch(BaseException e){
            return new BaseResponse(e.getStatus());
        }
    }


}
