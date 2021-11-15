package com.project.api_server.memo.dto;

import lombok.Data;

import javax.validation.constraints.Digits;

@Data
public class MoveMemoRequestDto {
    @Digits(integer = 2,fraction = 5)
    private double x;

    @Digits(integer = 2,fraction = 5)
    private double y;
}
