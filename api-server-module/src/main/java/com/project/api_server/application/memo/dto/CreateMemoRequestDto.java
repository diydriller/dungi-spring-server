package com.project.api_server.application.memo.dto;

import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

@Data
public class CreateMemoRequestDto {

    @NotEmpty(message = "memo is empty")
    private String memo;

    @NotEmpty(message = "color is empty")
    private String memoColor;

    @Digits(integer = 2,fraction = 5)
    private double x;

    @Digits(integer = 2,fraction = 5)
    private double y;
}
