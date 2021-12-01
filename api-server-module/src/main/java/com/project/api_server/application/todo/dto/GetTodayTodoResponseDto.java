package com.project.api_server.application.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetTodayTodoResponseDto {
    private Long todoId;
    private String todo;
    private String deadline;
    private Boolean isOwner;
}
