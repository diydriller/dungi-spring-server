package com.project.common.repository;

import com.project.common.model.DeleteStatus;
import com.project.common.model.Room;
import com.project.common.model.User;
import com.project.common.model.UserRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRoomRepository extends CrudRepository<UserRoom,Long> {

    Optional<UserRoom> findByUserAndRoom(User user, Room room);


    @Query("select r from UserRoom ur " +
            " join ur.room r " +
            " where ur.user=:user and r.id=:roomId and r.deleteStatus=:status " +
            " and ur.deleteStatus=:status ")
    Optional<Room> findRoomByUserAndRoomId(@Param("user") User user,
                                        @Param("roomId") Long roomId,
                                        @Param("status") DeleteStatus status);

    @Query("select count(ur) from UserRoom ur where ur.room=:room and ur.deleteStatus=:status")
    int countRoomUserByDeleteStatus(@Param("room") Room room,@Param("status") DeleteStatus status);
}
