package com.project.api_server.application.room.dto;

import com.project.common.dto.GetRoomUserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class GetRoomResponseDto {

    private List<RoomInfo> roomInfo;
    private String userName;

    @AllArgsConstructor
    @Data
    public static class RoomInfo{
        private Long roomId;
        private String roomName;
        private String roomColor;
        private List<GetRoomUserDto> members;
    }
}
