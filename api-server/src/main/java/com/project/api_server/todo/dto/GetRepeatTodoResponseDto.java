package com.project.api_server.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetRepeatTodoResponseDto {
    private Long todoId;
    private String todo;
    private String deadline;
    private Boolean isOwner;
    private String day;
}
