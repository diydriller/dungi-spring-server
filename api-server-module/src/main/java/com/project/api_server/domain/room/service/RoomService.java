package com.project.api_server.domain.room.service;

import com.project.api_server.application.room.dto.CreateRoomRequestDto;
import com.project.api_server.application.room.dto.GetRoomResponseDto;
import com.project.common.model.User;

public interface RoomService {

    public void createRoom(CreateRoomRequestDto roomRequestDto, User user);
    public void enterRoom(User user, Long roomId);
    public void leaveRoom(User user,Long roomId);
    public GetRoomResponseDto getRoom(User user, int page);
}
