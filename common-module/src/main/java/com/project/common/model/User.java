package com.project.common.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.common.error.BaseException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.project.common.response.BaseResponseStatus.INVALID_VALUE;

@Entity
@Getter
@NoArgsConstructor
@ToString(exclude={"memoList","userRoomList","todoList","noticeVoteList","userVoteItemList"})
@Table(name="Users",indexes = @Index(name="user_idx",columnList = "email",unique = true))
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Builder
    public User(String name, String nickname, String email, String password, String phoneNumber, String profileImg,String provider) {

        if(StringUtils.isEmpty(nickname)) throw new BaseException(INVALID_VALUE);
        if(StringUtils.isEmpty(email)) throw new BaseException(INVALID_VALUE);
        if(StringUtils.isEmpty(profileImg)) throw new BaseException(INVALID_VALUE);

        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profileImg = profileImg;
        this.provider=provider;
        this.deleteStatus=DeleteStatus.NOT_DELETED;
        this.bestMateCount=0;
    }

    public User(Long id){
        this.id=id;
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
