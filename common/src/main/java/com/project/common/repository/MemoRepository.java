package com.project.common.repository;


import com.project.common.model.DeleteStatus;
import com.project.common.model.Memo;
import com.project.common.model.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemoRepository extends CrudRepository<Memo,Long> {


    Memo save(Memo memo);

    @Query("select distinct m from Memo m join fetch m.user u join u.userRoomList ur " +
            "where m.room=:room and m.deleteStatus=:status and ur.deleteStatus=:status ")
    List<Memo> findAllMemoByRoomAndDeleteStatus(@Param("room") Room room,
                                                @Param("status") DeleteStatus status);

    @Query("select m from Memo m join m.user u join fetch m.user u join u.userRoomList ur " +
            "where m.id=:memoId and m.deleteStatus=:status and ur.deleteStatus=:status ")
    Optional<Memo> findMemoByIdAndDeleteStatus(@Param("memoId") Long memoId,
                                               @Param("status") DeleteStatus status);

}
