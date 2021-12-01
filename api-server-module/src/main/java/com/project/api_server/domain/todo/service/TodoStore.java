package com.project.api_server.domain.todo.service;

import com.project.api_server.application.todo.dto.CreateRepeatTodoRequestDto;
import com.project.api_server.application.todo.dto.CreateTodayTodoRequestDto;
import com.project.api_server.application.todo.dto.GetRepeatTodoResponseDto;
import com.project.api_server.application.todo.dto.GetTodayTodoResponseDto;
import com.project.common.model.Room;
import com.project.common.model.User;

import java.util.List;

public interface TodoStore {
    void saveTodayTodo(CreateTodayTodoRequestDto requestDto, User user, Room room);

    void saveRepeatTodo(CreateRepeatTodoRequestDto requestDto, User user, Room room);

    List<GetTodayTodoResponseDto> findTodayTodo(Room room, User user, int page);

    List<GetRepeatTodoResponseDto> findRepeatTodo(Room room, User user, int page);
}
