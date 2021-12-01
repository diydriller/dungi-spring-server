package com.project.common.repository;

import com.project.common.model.DeleteStatus;
import com.project.common.model.User;
import com.project.common.model.Vote;
import com.project.common.model.VoteItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteItemRepository extends CrudRepository<VoteItem,Long> {
    @Query("select distinct vi from VoteItem vi left join fetch vi.userVoteItemList uvi " +
            " where vi.vote=:vote ")
    List<VoteItem> findVoteItemByVote(@Param("vote") Vote vote);

    @Query("select u from UserVoteItem uvi join uvi.user u "+
            " where uvi.voteItem=:voteItem and uvi.deleteStatus=:status ")
    List<User> findVoteUserAndDeleteStatus(@Param("voteItem") VoteItem voteItem,
                            @Param("status") DeleteStatus deleteStatus);

    @Query("select vi from VoteItem vi " +
            " join vi.vote v " +
            " where v.id=:voteId and vi.id=:choiceId ")
    Optional<VoteItem> findByVote(@Param("voteId") Long voteId,
                                  @Param("choiceId") Long ChoiceId);

}
