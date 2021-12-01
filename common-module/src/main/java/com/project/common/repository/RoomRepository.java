package com.project.common.repository;

import com.project.common.model.DeleteStatus;
import com.project.common.model.FinishStatus;
import com.project.common.model.Room;
import com.project.common.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<Room,Long> {

    Room save(Room room);

    Optional<Room> findByIdAndDeleteStatus(Long id, DeleteStatus status);

    @Query("select distinct r from Room r join r.userRoomList ur " +
            "where ur.user=:user and ur.deleteStatus=:status")
    List<Room> findAllRoomByUserId(@Param("user") User user,
                                                @Param("status") DeleteStatus status,
                                                Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "update users u " +
            "set u.best_mate_count=u.best_mate_count+1 " +
            "where u.users_id in " +
                "(select ur2.users_id " +
                "from UserRoom ur2 inner join Todo t2 on (ur2.room_id=t2.room_id and ur2.users_id=t2.users_id) " +
                "where ur2.delete_status=:#{#delete_status?.name()} and t2.todo_status=:#{#finish_status?.name()} "+
                "and t2.dealine >= :start_date and t2.dealine < :end_date "+
                "group by ur2.room_id,ur2.users_id " +
                "having (ur2.room_id,COUNT(*))  in " +
                    "(select cnt.room_id,MAX(CNT) as MAX_CNT " +
                    "from (select ur.users_room_id,ur.room_id,ur.users_id,COUNT(*) as CNT " +
                        "from UserRoom ur inner join Todo t on (ur.room_id=t.room_id and ur.users_id=t.users_id) "+
                        "where ur.delete_status=:#{#delete_status?.name()} and t.todo_status=:#{#finish_status?.name()} "+
                        "and t.dealine >= :start_date and t.dealine < :end_date "+
                        "group by ur.room_id,ur.users_id) as cnt " +
                        "group by cnt.room_id))")
    void updateChart(@Param("start_date") LocalDateTime startDate,
                     @Param("end_date") LocalDateTime endDate,
                     @Param("delete_status") DeleteStatus deleteStatus,
                     @Param("finish_status") FinishStatus finishStatus);


    @Query(nativeQuery = true,value = "select from UserRoom ur inner join  ")
    User findBestMate(@Param("roomId") Long roomId);



}
