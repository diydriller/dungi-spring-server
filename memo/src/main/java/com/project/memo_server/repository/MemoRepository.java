package com.project.memo_server.repository;


import com.project.common.model.DeleteStatus;
import com.project.common.model.Memo;
import com.project.common.model.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface MemoRepository extends CrudRepository<Memo,Long> {

    Optional<Memo> findById(Long id);
    Memo save(Memo memo);

    @Query("select m from Memo m join fetch m.user " +
            "where m.room=:room and m.deleteStatus=:status")
    List<Memo> findAllMemoByRoomAndDeleteStatus(@Param("room") Room room,
                                                @Param("status") DeleteStatus status);
}
