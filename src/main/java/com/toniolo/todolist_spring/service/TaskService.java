package com.toniolo.todolist_spring.service;

import com.toniolo.todolist_spring.model.TaskModel;
import com.toniolo.todolist_spring.model.TaskStatus;
import com.toniolo.todolist_spring.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<TaskModel> getTasks() {
        return taskRepository.findAll();
    }

    public Optional<TaskModel> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public TaskModel createTask(TaskModel task) {
        task.setStatus(TaskStatus.PENDING);
        return taskRepository.save(task);
    }

    public Optional<TaskModel> markTaskAsDone(Long id) {
        Optional<TaskModel> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            TaskModel task = taskOptional.get();
            task.setStatus(TaskStatus.COMPLETED);
            taskRepository.save(task);
            return Optional.of(task);
        }
        return Optional.empty();
    }

    public Optional<TaskModel> markTaskAsInProgress(Long id) {
        Optional<TaskModel> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            TaskModel task = taskOptional.get();
            task.setStatus(TaskStatus.IN_PROGRESS);
            taskRepository.save(task);
            return Optional.of(task);
        }
        return Optional.empty();
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}




