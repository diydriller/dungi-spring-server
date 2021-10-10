package com.project.common.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@DiscriminatorValue(value = "V")
public class Vote extends NoticeVote{

    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name="vote_status")
    private FinishStatus finishStatus;

    @OneToMany(mappedBy = "vote",cascade = CascadeType.ALL)
    private List<VoteItem> voteItemList=new ArrayList<>();

    public void addVoteItem(VoteItem voteItem){
        voteItemList.add(voteItem);
        voteItem.setVote(this);
    }


    public static Vote createVote(Room room,User user,String title,
                                  List<VoteItem> voteItems){
        Vote vote=new Vote();
        vote.setRoom(room);
        vote.setUser(user);
        vote.setFinishStatus(FinishStatus.UNFINISHED);
        vote.setTitle(title);
        for(VoteItem voteItem:voteItems){
            vote.addVoteItem(voteItem);
        }

        return vote;
    }
}
