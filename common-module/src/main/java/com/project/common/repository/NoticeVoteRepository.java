package com.project.common.repository;

import com.project.common.dto.GetNoticeVoteDto;
import com.project.common.model.DeleteStatus;
import com.project.common.model.NoticeVote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NoticeVoteRepository extends CrudRepository<NoticeVote,Long> {

    NoticeVote save(NoticeVote noticeVote);

    @Query(value = "select nv.DTYPE as type,nv.notice_vote_id as id,u.profile_img as profileImg,nv.notice_item as notice,u.users_id as userId,nv.title as title,nv.created_time as createdAt " +
            " from NoticeVote nv " +
            " inner join Room r on nv.room_id=r.room_id " +
            " inner join users u on nv.users_id=u.users_id " +
            " where nv.room_id=:roomId and nv.delete_status=:#{#status?.name()} ",nativeQuery = true,
    countQuery = "select count(*) from NoticeVote ")
    Page<GetNoticeVoteDto> findAllNoticeVoteByRoomAndDeleteStatus(@Param("roomId") Long roomId,
                                                                  @Param("status") DeleteStatus status,
                                                                  Pageable pageable);


}
