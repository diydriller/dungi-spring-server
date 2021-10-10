package com.project.common.model;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@DiscriminatorValue(value = "T")
public class TodayTodo extends Todo{

    @Enumerated(EnumType.STRING)
    @Column(name="todo_status")
    private FinishStatus finishStatus;

    public static TodayTodo createTodo(Room room, User user, FinishStatus finishStatus,
                                  LocalDateTime dealine, String todoItem){
        TodayTodo todayTodo=new TodayTodo();
        todayTodo.setRoom(room);
        todayTodo.setUser(user);
        todayTodo.setDealine(dealine);
        todayTodo.setFinishStatus(finishStatus);
        todayTodo.setTodoItem(todoItem);
        todayTodo.setDeleteStatus(DeleteStatus.NOT_DELETED);
        return todayTodo;
    }
}
