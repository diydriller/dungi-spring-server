package com.project.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class VoteItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="vote_item_id")
    private Long id;

    private String choice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="notice_vote_id")
    private Vote vote;

    @OneToMany(mappedBy = "voteItem")
    private List<UserVoteItem> userVoteItemList=new ArrayList<>();

    public static VoteItem createVoteItem(String choice){
        VoteItem voteItem=new VoteItem();
        voteItem.setChoice(choice);
        return voteItem;
    }


}
