package com.project.room_server.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CreateRoomRequestDto {

    @NotEmpty(message = "color is empty")
    private String color;

    @NotEmpty(message = "name is empty")
    @Size(max=10,message = "name's max length is 10")
    private String name;
}
