package com.project.common.repository;

import com.project.common.model.DeleteStatus;
import com.project.common.model.FinishStatus;
import com.project.common.model.Room;
import com.project.common.model.User;
import javassist.LoaderClassPath;
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

    Optional<Room> findByIdAndDeleteStatus(Long id, DeleteStatus status);

    // 사용자가 들어있는 방 가져오기
    @Query("SELECT DISTINCT r FROM Room r JOIN r.userRoomList ur " +
            "WHERE ur.user=:user AND ur.deleteStatus=:status")
    List<Room> findAllRoomByUserId(@Param("user") User user,
                                                @Param("status") DeleteStatus status,
                                                Pageable pageable);


    // 방에서 일주일동안 집안일 가장 많이 한 멤버 가져오기
    @Query(nativeQuery = true,value =
            "WITH CountTodo AS " +
                "(SELECT COUNT(t.todo_id) AS cnt,t.users_id " +
                "FROM Todo t " +
                "WHERE t.room_id=:room_id AND t.dealine>:start_date AND t.dealine<:end_date AND t.todo_status=:#{#finish_status?.name()} " +
                "GROUP BY t.users_id) " +
            "SELECT u.users_id FROM Users u WHERE u.users_id in " +
                    "(SELECT ct1.users_id FROM CountTodo ct1 "  +
                    "INNER JOIN (SELECT MAX(cnt) AS cnt,users_id FROM CountTodo GROUP BY users_id) ct2 ON(ct1.cnt=ct2.cnt)) " +
                    "LIMIT 0,1 ")
    Object[] findBestMate(
            @Param("room_id") Long roomId,
                      @Param("start_date") LocalDateTime startDate,
                      @Param("end_date") LocalDateTime endDate,
                      @Param("finish_status") FinishStatus finishStatus);



}
