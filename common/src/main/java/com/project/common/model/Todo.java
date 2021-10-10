package com.project.common.model;

import lombok.Data;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@DiscriminatorColumn
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Todo extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="todo_id")
    private Long id;

    @Column(name="todo_item")
    private String todoItem;


    private LocalDateTime dealine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="users_id")
    private User user;



    @Enumerated(EnumType.STRING)
    @Column(name="delete_status")
    private DeleteStatus deleteStatus;


    public void setRoom(Room room){
        this.room=room;
        room.getTodoList().add(this);
    }

    public void setUser(User user){
        this.user=user;
        user.getTodoList().add(this);
    }






}

