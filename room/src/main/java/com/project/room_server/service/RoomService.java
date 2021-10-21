package com.project.room_server.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.common.dto.GetRoomUserDto;
import com.project.common.error.BaseException;
import com.project.common.model.DeleteStatus;
import com.project.common.model.Room;
import com.project.common.model.User;
import com.project.common.model.UserRoom;
import com.project.common.repository.RoomRepository;
import com.project.common.repository.UserRepository;
import com.project.common.repository.UserRoomRepository;
import com.project.common.service.RedisService;
import com.project.room_server.dto.CreateRoomRequestDto;
import com.project.room_server.dto.GetRoomResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.project.common.response.BaseResponseStatus.*;

@Service
@AllArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;
    private final RedisService redisService;


    // 방생성
    @Transactional
    public void createRoom(CreateRoomRequestDto roomRequestDto,Long userId) throws IOException,BaseException {

        User user=redisService.getUser("login_"+userId);
        UserRoom userRoom=UserRoom.createUserRoom(user);
        Room room=Room.createRoom(roomRequestDto.getName(),roomRequestDto.getColor(),
                userRoom);
        roomRepository.save(room);
    }

    // 방입장
    @Transactional
    public void enterRoom(Long userId,Long roomId) throws IOException,BaseException {

        User user=redisService.getUser("login_"+userId);
        Room room = roomRepository.findByIdAndDeleteStatus(roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(() -> {
                    throw new BaseException(NOT_EXIST_ROOM);
                });

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

    // 방퇴장 , 방 삭제
    @Transactional
    public void leaveRoom(Long userId,Long roomId) throws IOException,BaseException {

        User user=redisService.getUser("login_"+userId);
        Room room = roomRepository.findByIdAndDeleteStatus(roomId,DeleteStatus.NOT_DELETED)
                .orElseThrow(() -> {
                    throw new BaseException(NOT_EXIST_ROOM);
                });

        UserRoom userRoom=userRoomRepository.findByUserAndRoom(user,room)
                .orElseThrow(()->{
                    throw new BaseException(NOT_EXIST_USER_ROOM);
                });
        userRoom.setDeleteStatus(DeleteStatus.DELETED);
        userRoomRepository.save(userRoom);

        Long num=userRoomRepository.countRoomUserByDeleteStatus(room,DeleteStatus.NOT_DELETED);

        if(num<=0){
            room.setDeleteStatus(DeleteStatus.DELETED);
            roomRepository.save(room);
        }
    }

    // 방조회
    @Transactional
    public GetRoomResponseDto getRoom(Long userId,int page) throws IOException,BaseException {
        PageRequest pageRequest = PageRequest.of(page,5, Sort.Direction.DESC, "createdTime");

        User user=redisService.getUser("login_"+userId);

        Page<Room> roomList=roomRepository.findAllRoomByUserAndDeleteStatus(user.getId(),DeleteStatus.NOT_DELETED,pageRequest);

        GetRoomResponseDto res=new GetRoomResponseDto();
        List<GetRoomResponseDto.RoomInfo> roomInfoList=new ArrayList<>();

        for(Room room: roomList){
            List<GetRoomUserDto> roomUser=userRepository.findRoomUser(room);

            GetRoomResponseDto.RoomInfo roomInfo=new GetRoomResponseDto.RoomInfo(room.getId(),
                    room.getName(), room.getColor(),roomUser);
            roomInfoList.add(roomInfo);
        }

        res.setRoomInfo(roomInfoList);
        res.setUserName(user.getNickname());
        return res;
    }

}
