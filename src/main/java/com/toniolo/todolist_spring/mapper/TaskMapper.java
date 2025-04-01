package com.toniolo.todolist_spring.mapper;

import com.toniolo.todolist_spring.dto.TaskRequestDTO;
import com.toniolo.todolist_spring.dto.TaskResponseDTO;
import com.toniolo.todolist_spring.dto.UserResponseDTO;
import com.toniolo.todolist_spring.model.TaskModel;
import com.toniolo.todolist_spring.model.UserModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public TaskModel toEntity(TaskRequestDTO dto) {
        TaskModel task = new TaskModel();
        task.setDescription(dto.getDescription());
        return task;
    }

    public TaskResponseDTO toDto(TaskModel entity) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());

        if (entity.getUser() != null) {
            UserResponseDTO userDto = new UserResponseDTO();
            userDto.setId(entity.getUser().getId());
            userDto.setUsername(entity.getUser().getUsername());
            userDto.setRole(entity.getUser().getRole().name());
            dto.setUser(userDto);
        }

        return dto;
    }

    public List<TaskResponseDTO> toDtoList(List<TaskModel> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}