package com.project.common.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@DiscriminatorValue(value = "R")
public class RepeatTodo extends Todo{

    @OneToMany(mappedBy = "repeatTodo",cascade = CascadeType.ALL)
    private List<RepeatDay> repeatDayList=new ArrayList<>();

    public void addRepeatDay(RepeatDay repeatDay){
        repeatDayList.add(repeatDay);
        repeatDay.setRepeatTodo(this);
    }



    public static RepeatTodo createRepeatTodo(Room room, User user, LocalDateTime dealine,
                                              String todoItem,List<RepeatDay> repeatDays){
        RepeatTodo repeatTodo=new RepeatTodo();
        repeatTodo.setRoom(room);
        repeatTodo.setUser(user);
        repeatTodo.setDealine(dealine);
        repeatTodo.setTodoItem(todoItem);
        repeatTodo.setDeleteStatus(DeleteStatus.NOT_DELETED);
        for(RepeatDay repeatDay:repeatDays){
            repeatTodo.addRepeatDay(repeatDay);
        }
        return repeatTodo;
    }
}
