package com.toniolo.todolist_spring.service;

import com.toniolo.todolist_spring.model.TaskModel;
import com.toniolo.todolist_spring.model.UserModel;
import com.toniolo.todolist_spring.model.UserRole;
import org.springframework.stereotype.Component;

@Component
public class TaskAuthorizationService {

    public boolean isAdminOrOwner(UserModel currentUser, TaskModel task) {
        return currentUser.getRole() == UserRole.ADMIN ||
                (task.getUser() != null && task.getUser().getId().equals(currentUser.getId()));
    }
}