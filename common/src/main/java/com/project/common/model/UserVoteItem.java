package com.project.common.model;

import javax.persistence.*;

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
}
