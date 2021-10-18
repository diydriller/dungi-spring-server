package com.project.todo_server.repository;

import com.project.common.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TodoRepository extends CrudRepository<Todo,Long> {

    Todo save(Todo todo);

    Optional<Todo> findById(Long id);

    @Query(value = "select tt from TodayTodo tt join fetch tt.user u " +
            "where tt.room=:room and tt.deleteStatus=:deleteStatus " +
            "and tt.finishStatus=:finishStatus and tt.dealine>=:currentTime",
    countQuery = "select count(tt.id) from TodayTodo tt")
    Page<TodayTodo> findAllTodayTodoByRoomAndDeleteStatusAndTodoStatus(@Param("room") Room room,
                                                                      @Param("deleteStatus")DeleteStatus deleteStatus,
                                                                      @Param("finishStatus") FinishStatus finishStatus,
                                                                      @Param("currentTime")LocalDateTime currentTime,
                                                                      Pageable pageable);


    @Query(value="select rt from RepeatTodo rt join fetch rt.user " +
            /*"join fetch rt.repeatDayList "+*/
            "where rt.room=:room and rt.deleteStatus=:status",
    countQuery = "select count(rt.id) from RepeatTodo rt")
    Page<RepeatTodo> findAllRepeatTodoByRoomAndDeleteStatus(@Param("room") Room room,
                                                           @Param("status") DeleteStatus status,
                                                           Pageable pageable);


}
