package com.toniolo.todolist_spring.controller;

import com.toniolo.todolist_spring.dto.TaskRequestDTO;
import com.toniolo.todolist_spring.dto.TaskResponseDTO;
import com.toniolo.todolist_spring.mapper.TaskMapper;
import com.toniolo.todolist_spring.model.TaskModel;
import com.toniolo.todolist_spring.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTasks() {
        List<TaskModel> tasks = taskService.getTasks();

        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskMapper.toDtoList(tasks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        Optional<TaskModel> task = taskService.getTaskById(id);
        return task.map(t -> ResponseEntity.ok(taskMapper.toDto(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskDto) {
        TaskModel taskModel = taskMapper.toEntity(taskDto);
        TaskModel createdTask = taskService.createTask(taskModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskMapper.toDto(createdTask));
    }

    @PutMapping("/{id}/status/in-progress")
    public ResponseEntity<TaskResponseDTO> markTaskAsInProgress(@PathVariable Long id) {
        Optional<TaskModel> updatedTask = taskService.markTaskAsInProgress(id);
        return updatedTask.map(t -> ResponseEntity.ok(taskMapper.toDto(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status/completed")
    public ResponseEntity<TaskResponseDTO> markTaskAsCompleted(@PathVariable Long id) {
        Optional<TaskModel> updatedTask = taskService.markTaskAsDone(id);
        return updatedTask.map(t -> ResponseEntity.ok(taskMapper.toDto(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}