package com.project.api_server.domain.room.service;

import com.project.api_server.application.room.dto.CreateRoomRequestDto;
import com.project.api_server.application.room.dto.GetRoomResponseDto;
import com.project.common.model.Room;
import com.project.common.model.User;

public interface RoomStore {
    Room findRoomEntered(User user,Long roomId);
    void saveRoom(User user, CreateRoomRequestDto requestDto);
    void enterRoom(User user, Room room);
    Room checkRoomPresent(Long roomId);
    void deleteRoom(User user,Room room);
    GetRoomResponseDto findAllRoom(User user,int page);
}
