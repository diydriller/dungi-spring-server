package com.project.common.model;


import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class NoticeVote extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="notice_vote_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="users_id")
    private User user;



    @Enumerated(EnumType.STRING)
    @Column(name="delete_status")
    private DeleteStatus deleteStatus;


    public void setRoom(Room room){
        this.room=room;
        room.getNoticeVoteList().add(this);
    }

    public void setUser(User user){
        this.user=user;
        user.getNoticeVoteList().add(this);
    }

}

