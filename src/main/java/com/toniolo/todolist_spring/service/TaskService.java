package com.toniolo.todolist_spring.service;

import com.toniolo.todolist_spring.model.TaskModel;
import com.toniolo.todolist_spring.model.TaskStatus;
import com.toniolo.todolist_spring.model.UserModel;
import com.toniolo.todolist_spring.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAuthorizationService authorizationService;

    public List<TaskModel> getTasks() {
        UserModel currentUser = getCurrentUser();

        if (currentUser.getRole().name().equals("ADMIN")) {
            return taskRepository.findAll();
        }

        return taskRepository.findByUserId(currentUser.getId());
    }

    public Optional<TaskModel> getTaskById(Long id) {
        Optional<TaskModel> taskOpt = taskRepository.findById(id);

        if (taskOpt.isPresent()) {
            TaskModel task = taskOpt.get();
            UserModel currentUser = getCurrentUser();

            if (authorizationService.isAdminOrOwner(currentUser, task)) {
                return taskOpt;
            } else {
                throw new AccessDeniedException("Não autorizado a acessar esta task");
            }
        }

        return taskOpt;
    }

    public TaskModel createTask(TaskModel task) {
        UserModel currentUser = getCurrentUser();
        task.setUser(currentUser);
        task.setStatus(TaskStatus.PENDING);
        return taskRepository.save(task);
    }

    public Optional<TaskModel> markTaskAsInProgress(Long id) {
        return updateTaskStatus(id, "IN_PROGRESS");
    }

    public Optional<TaskModel> markTaskAsDone(Long id) {
        return updateTaskStatus(id, "COMPLETED");
    }

    private Optional<TaskModel> updateTaskStatus(Long id, String status) {
        Optional<TaskModel> taskOpt = taskRepository.findById(id);

        if (taskOpt.isPresent()) {
            TaskModel task = taskOpt.get();
            UserModel currentUser = getCurrentUser();

            if (authorizationService.isAdminOrOwner(currentUser, task)) {
                task.setStatus(TaskStatus.valueOf(status));
                return Optional.of(taskRepository.save(task));
            } else {
                throw new AccessDeniedException("Não autorizado a modificar esta task");
            }
        }

        return Optional.empty();
    }

    public void deleteTask(Long id) {
        Optional<TaskModel> taskOpt = taskRepository.findById(id);

        if (taskOpt.isPresent()) {
            TaskModel task = taskOpt.get();
            UserModel currentUser = getCurrentUser();

            if (authorizationService.isAdminOrOwner(currentUser, task)) {
                taskRepository.deleteById(id);
            } else {
                throw new AccessDeniedException("Não autorizado a deletar esta task");
            }
        }
    }

    private UserModel getCurrentUser() {
        return (UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}