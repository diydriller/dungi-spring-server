package com.project.common.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "UserRoom")
@Entity
@Data
public class UserRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="users_room_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="users_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(name="delete_status")
    private DeleteStatus deleteStatus;

    public void setUser(User user){
        this.user=user;
//        user.getUserRoomList().add(this);
    }

    public void setRoom(Room room){
        this.room=room;
        room.getUserRoomList().add(this);
    }

    public static UserRoom createUserRoom(User user){
        UserRoom userRoom=new UserRoom();
        userRoom.setUser(user);
        userRoom.setDeleteStatus(DeleteStatus.NOT_DELETED);
        return userRoom;
    }

    public static UserRoom createUserRoom(User user,Room room){
        UserRoom userRoom=new UserRoom();
        userRoom.setUser(user);
        userRoom.setRoom(room);
        userRoom.setDeleteStatus(DeleteStatus.NOT_DELETED);
        return userRoom;
    }



}
