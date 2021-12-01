package com.project.common.repository;

import com.project.common.model.DeleteStatus;
import com.project.common.model.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote,Long> {

    @Query("select distinct v from Vote v join fetch v.user u join u.userRoomList ur " +
            " where v.id=:voteId and v.deleteStatus=:status and ur.deleteStatus=:status ")
    Optional<Vote> findByIdAndDeleteStatus(@Param("voteId") Long voteId,
                                           @Param("status") DeleteStatus deleteStatus);

}
