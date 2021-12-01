package com.project.api_server.domain.room.service;


import com.project.common.model.Room;
import com.project.common.model.User;
import com.project.api_server.application.room.dto.CreateRoomRequestDto;
import com.project.api_server.application.room.dto.GetRoomResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomStore roomStore;

    // 방생성
    @Transactional
    public void createRoom(CreateRoomRequestDto requestDto,User user) {
        roomStore.saveRoom(user,requestDto);
    }

    // 방입장
    @Transactional
    public void enterRoom(User user,Long roomId) {
        Room room = roomStore.checkRoomPresent(roomId);
        roomStore.enterRoom(user,room);
    }

    // 방퇴장 , 방 삭제
    @Transactional
    public void leaveRoom(User user,Long roomId)  {
        Room room = roomStore.findRoomEntered(user,roomId);
        roomStore.deleteRoom(user,room);
    }

    // 방조회
    @Transactional
    public GetRoomResponseDto getRoom(User user,int page) {
        return roomStore.findAllRoom(user,page);
    }

}
