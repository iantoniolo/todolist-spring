package com.toniolo.todolist_spring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="tasks")
public class TaskModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String description;

    @Enumerated(EnumType.STRING)
    private com.toniolo.todolist_spring.model.TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
}
