package com.project.api_server.domain.todo.service;

import com.project.api_server.application.todo.dto.CreateRepeatTodoRequestDto;
import com.project.api_server.application.todo.dto.CreateTodayTodoRequestDto;
import com.project.api_server.application.todo.dto.GetRepeatTodoResponseDto;
import com.project.api_server.application.todo.dto.GetTodayTodoResponseDto;
import com.project.common.model.User;

import java.util.List;


public interface TodoService {


    public void createTodayTodo(CreateTodayTodoRequestDto todoRequestDto, User user, Long roomId);

    //반복할일 생성
    public void createRepeatTodo(CreateRepeatTodoRequestDto todoRequestDto, User user, Long roomId);

    // 하루 할일 조회

    List<GetTodayTodoResponseDto> getTodayTodo(User user, Long roomId, int page);

    // 반복 할일 조회
    List<GetRepeatTodoResponseDto> getRepeatTodo(User user, Long roomId, int page);
}
