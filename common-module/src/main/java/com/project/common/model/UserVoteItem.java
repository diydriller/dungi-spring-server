package com.project.common.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class UserVoteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="users_vote_item_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="vote_item_id")
    private VoteItem voteItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="users_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name="delete_status")
    private DeleteStatus deleteStatus;

    public void setUser(User user){
        this.user=user;
        user.getUserVoteItemList().add(this);
    }

    public void setVoteItem(VoteItem voteItem){
        this.voteItem=voteItem;
        voteItem.getUserVoteItemList().add(this);
    }

    public static UserVoteItem createUserVoteItem(User user,VoteItem voteItem){
        UserVoteItem userVoteItem=new UserVoteItem();
        userVoteItem.setUser(user);
        userVoteItem.setVoteItem(voteItem);
        userVoteItem.setDeleteStatus(DeleteStatus.NOT_DELETED);
        return userVoteItem;
    }



}
