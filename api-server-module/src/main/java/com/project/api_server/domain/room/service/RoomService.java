package com.project.api_server.domain.room.service;

import com.project.api_server.application.room.dto.CreateRoomRequestDto;
import com.project.api_server.application.room.dto.GetRoomResponseDto;
import com.project.common.model.User;

public interface RoomService {

    void createRoom(CreateRoomRequestDto roomRequestDto, User user);
    void enterRoom(User user, Long roomId);
    void leaveRoom(User user,Long roomId);
    GetRoomResponseDto getRoom(User user, int page);
}
