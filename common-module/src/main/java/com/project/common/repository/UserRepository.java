package com.project.common.repository;

import com.project.common.dto.GetRoomUserDto;
import com.project.common.model.Room;
import com.project.common.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long>{
    Optional<User> findByEmail(String email);
    @Query("select new com.project.common.dto.GetRoomUserDto(u.profileImg,u.nickname) " +
            " from Users u join u.userRoomList ur " +
            " where ur.room=:room")
    List<GetRoomUserDto> findRoomUser(Room room);
}