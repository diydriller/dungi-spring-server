package com.project.common.repository;

import com.project.common.model.User;
import com.project.common.model.UserVoteItem;
import com.project.common.model.VoteItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserVoteItemRepository extends CrudRepository<UserVoteItem,Long> {


    @Query("select uvi from UserVoteItem uvi " +
            " where uvi.user=:user and uvi.voteItem=:voteItem")
    Optional<UserVoteItem> findByUserAndVoteItem(@Param("user") User user,
                                  @Param("voteItem") VoteItem voteItem);

}
