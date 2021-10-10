package com.project.common.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Entity
@Data
@ToString(exclude={"memoList","userRoomList","todoList"})
public class Room extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id")
    private Long id;

    private String name;

    private String color;

    @Enumerated(EnumType.STRING)
    @Column(name="delete_status")
    private DeleteStatus deleteStatus;

    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
    private List<UserRoom> userRoomList=new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<Todo> todoList=new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<Memo> memoList=new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<NoticeVote> noticeVoteList=new ArrayList<>();

    void setUserRoom(UserRoom userRoom){
        userRoomList.add(userRoom);
        userRoom.setRoom(this);
    }

    public static Room createRoom(String name,String color,UserRoom... userRooms){
        Room room = new Room();
        room.setColor(color);
        room.setName(name);
        room.setDeleteStatus(DeleteStatus.NOT_DELETED);
        for(UserRoom userRoom:userRooms){
            room.setUserRoom(userRoom);
        }
        return room;
    }
}
