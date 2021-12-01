package com.project.api_server.domain.todo.service;

import com.project.api_server.domain.room.service.RoomStore;

import com.project.common.model.*;

import com.project.api_server.application.todo.dto.CreateRepeatTodoRequestDto;
import com.project.api_server.application.todo.dto.CreateTodayTodoRequestDto;
import com.project.api_server.application.todo.dto.GetRepeatTodoResponseDto;
import com.project.api_server.application.todo.dto.GetTodayTodoResponseDto;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Slf4j
@Service
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoStore todoStore;
    private final RoomStore roomStore;

    // 유저가 방에 입장해있는지 검증 - 오늘 할일 생성
    @Transactional
    public void createTodayTodo(CreateTodayTodoRequestDto requestDto, User user, Long roomId) {
        Room room = roomStore.findRoomEntered(user,roomId);
        todoStore.saveTodayTodo(requestDto,user,room);
    }

    // 유저가 방에 입장해있는지 검증 - 오늘 할일 생성
    @Transactional
    public void createRepeatTodo(CreateRepeatTodoRequestDto requestDto, User user, Long roomId) {
        Room room = roomStore.findRoomEntered(user,roomId);
        todoStore.saveRepeatTodo(requestDto,user,room);
    }

    // 유저가 방에 입장해있는지 검증 - 오늘 할일 조회
    @Transactional
    public List<GetTodayTodoResponseDto> getTodayTodo(User user,Long roomId, int page) {
        Room room = roomStore.findRoomEntered(user,roomId);
        return todoStore.findTodayTodo(room,user,page);
    }

    // 유저가 방에 입장해있는지 검증 - 반복 할일 조회
    public List<GetRepeatTodoResponseDto> getRepeatTodo(User user, Long roomId, int page) {
        Room room = roomStore.findRoomEntered(user,roomId);
        return todoStore.findRepeatTodo(room,user,page);
    }
}
