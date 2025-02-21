package com.toniolo.todolist_spring.controller;

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

    @GetMapping
    public ResponseEntity<List<TaskModel>> getTasks() {
        List<TaskModel> tasks = taskService.getTasks();

        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskModel> getTaskById(@PathVariable Long id) {
        Optional<TaskModel> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaskModel> createTask(@RequestBody TaskModel task) {
        TaskModel createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}/status/in-progress")
    public ResponseEntity<TaskModel> markTaskAsInProgress(@PathVariable Long id) {
        Optional<TaskModel> updatedTask = taskService.markTaskAsInProgress(id);
        return updatedTask.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status/completed")
    public ResponseEntity<TaskModel> markTaskAsCompleted(@PathVariable Long id) {
        Optional<TaskModel> updatedTask = taskService.markTaskAsDone(id);
        return updatedTask.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
