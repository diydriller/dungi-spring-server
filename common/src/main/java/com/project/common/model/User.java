package com.project.common.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name="users")
@Data
@NoArgsConstructor
@ToString(exclude={"memoList","userRoomList","todoList","noticeVoteList","userVoteItemList"})
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="users_id")
    private Long id;

    private String name;

    private String nickname;

    private String email;

    private String password;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="profile_img")
    private String profileImg;

    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(name="delete_status")
    private DeleteStatus deleteStatus;

    @Column(name="best_mate_count")
    private int bestMateCount;

    public User(String name, String nickname, String email, String password, String phoneNumber, String profileImg,String provider) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profileImg = profileImg;
        this.provider=provider;
        this.deleteStatus=DeleteStatus.NOT_DELETED;
    }

    public User(String nickname, String email, String profileImg, String provider) {
        this.nickname = nickname;
        this.email = email;
        this.profileImg = profileImg;
        this.provider = provider;
        this.deleteStatus=DeleteStatus.NOT_DELETED;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserRoom> userRoomList=new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Todo> todoList=new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Memo> memoList=new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<NoticeVote> noticeVoteList=new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserVoteItem> userVoteItemList=new ArrayList<>();


}
