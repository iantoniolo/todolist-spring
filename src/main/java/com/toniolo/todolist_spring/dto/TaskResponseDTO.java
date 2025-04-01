package com.toniolo.todolist_spring.dto;

import com.toniolo.todolist_spring.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {
    private long id;
    private String description;
    private TaskStatus status;
    private UserResponseDTO user;
}