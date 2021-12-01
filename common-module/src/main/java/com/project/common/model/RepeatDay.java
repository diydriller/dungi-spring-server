package com.project.common.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class RepeatDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="repeat_day_id")
    private Long id;

    private int day;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="todo_id")
    private RepeatTodo repeatTodo;


    public static RepeatDay createRepeatDay(int day){
        RepeatDay repeatDay=new RepeatDay();
        repeatDay.setDay(day);
        return repeatDay;
    }
}
