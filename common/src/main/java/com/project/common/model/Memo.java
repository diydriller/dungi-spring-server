package com.project.common.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Memo extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="memo_id")
    private Long id;

    @Column(name="memo_item")
    private String memoItem;
    @Column(name="x_position")
    private double xPosition;
    @Column(name="y_position")
    private double yPosition;
    @Column(name="memo_color")
    private String memoColor;

    @Enumerated(EnumType.STRING)
    @Column(name="delete_status")
    private DeleteStatus deleteStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;

    void setUser(User user){
        this.user=user;
        user.getMemoList().add(this);
    }

    void setRoom(Room room){
        this.room=room;
        room.getMemoList().add(this);
    }


    public static Memo createMemo(User user,Room room,String memoItem,double xPosition,double yPosition,String memoColor){
        Memo memo=new Memo();
        memo.setUser(user);
        memo.setRoom(room);
        memo.setMemoItem(memoItem);
        memo.setXPosition(xPosition);
        memo.setYPosition(yPosition);
        memo.setMemoColor(memoColor);
        memo.setDeleteStatus(DeleteStatus.NOT_DELETED);
        return memo;
    }


}
