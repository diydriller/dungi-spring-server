package com.project.api_server.infrastructure.room;


import com.project.api_server.application.room.dto.CreateRoomRequestDto;
import com.project.api_server.application.room.dto.GetRoomResponseDto;
import com.project.api_server.domain.room.service.RoomStore;
import com.project.common.dto.GetRoomUserDto;
import com.project.common.error.BaseException;
import com.project.common.model.DeleteStatus;
import com.project.common.model.Room;
import com.project.common.model.User;
import com.project.common.model.UserRoom;
import com.project.common.repository.RoomRepository;
import com.project.common.repository.UserRepository;
import com.project.common.repository.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.project.common.response.BaseResponseStatus.NOT_EXIST_ROOM;
import static com.project.common.response.BaseResponseStatus.NOT_EXIST_USER_ROOM;

@Component
@RequiredArgsConstructor
public class RoomStoreImpl implements RoomStore {

    private final UserRoomRepository userRoomRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Override
    public Room findRoomEntered(User user, Long roomId) {
        return userRoomRepository.findRoomByUserAndRoomId(user,roomId, DeleteStatus.NOT_DELETED)
                .orElseThrow(()->{throw new BaseException(NOT_EXIST_USER_ROOM);});
    }

    @Override
    public void saveRoom(User user, CreateRoomRequestDto requestDto) {
        UserRoom userRoom=UserRoom.createUserRoom(user);
        Room room=Room.createRoom(requestDto.getName(),requestDto.getColor(),
                userRoom);

        userRoomRepository.save(userRoom);
        roomRepository.save(room);
    }

    @Override
    public void enterRoom(User user, Room room) {
        userRoomRepository.findByUserAndRoom(user,room)
                .ifPresentOrElse(
                        (ur)->{
                            ur.setDeleteStatus(DeleteStatus.NOT_DELETED);
                        },
                        ()->{
                            UserRoom userRoom=UserRoom.createUserRoom(user,room);
                            userRoomRepository.save(userRoom);
                        }
                );
    }

    @Override
    public Room checkRoomPresent(Long roomId) {
        return roomRepository.findByIdAndDeleteStatus(roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(() -> {
                    throw new BaseException(NOT_EXIST_ROOM);
                });
    }

    @Override
    public void deleteRoom(User user, Room room) {
        UserRoom userRoom=userRoomRepository.findByUserAndRoom(user,room)
                .orElseThrow(()->{
                    throw new BaseException(NOT_EXIST_USER_ROOM);
                });
        userRoom.setDeleteStatus(DeleteStatus.DELETED);
        userRoomRepository.save(userRoom);

        int num=userRoomRepository.countRoomUserByDeleteStatus(room,DeleteStatus.NOT_DELETED);

        if(num<=0){
            room.setDeleteStatus(DeleteStatus.DELETED);
            roomRepository.save(room);
        }
    }

    @Override
    public GetRoomResponseDto findAllRoom(User user, int page) {
        PageRequest pageRequest = PageRequest.of(page,5, Sort.Direction.DESC, "createdTime");

        List<Room> roomList = roomRepository.findAllRoomByUserId(user,DeleteStatus.NOT_DELETED,pageRequest);

        GetRoomResponseDto res=new GetRoomResponseDto();
        List<GetRoomResponseDto.RoomInfo> roomInfoList=new ArrayList<>();

        for(Room room: roomList){
            List<GetRoomUserDto> roomUser = userRepository.findRoomUser(room);

            GetRoomResponseDto.RoomInfo roomInfo=new GetRoomResponseDto.RoomInfo(room.getId(),
                    room.getName(), room.getColor(),roomUser);
            roomInfoList.add(roomInfo);
        }

        res.setRoomInfo(roomInfoList);
        res.setUserName(user.getNickname());
        return res;
    }
}
