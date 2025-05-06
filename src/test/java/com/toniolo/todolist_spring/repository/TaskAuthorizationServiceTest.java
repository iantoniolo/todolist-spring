package com.toniolo.todolist_spring.service;

import com.toniolo.todolist_spring.model.TaskModel;
import com.toniolo.todolist_spring.model.UserModel;
import com.toniolo.todolist_spring.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskAuthorizationServiceTest {

    private final TaskAuthorizationService service = new TaskAuthorizationService();

    @Test
    @DisplayName("Deve retornar true se usuário for ADMIN")
    void testIsAdmin() {
        UserModel admin = new UserModel("admin", "senha", UserRole.ADMIN);
        TaskModel task = new TaskModel();
        assertTrue(service.isAdminOrOwner(admin, task));
    }

    @Test
    @DisplayName("Deve retornar true se usuário for owner da task")
    void testIsOwner() {
        UserModel user = new UserModel("user", "senha", UserRole.STANDARD);
        user.setId(1L);
        TaskModel task = new TaskModel();
        task.setUser(user);
        assertTrue(service.isAdminOrOwner(user, task));
    }

    @Test
    @DisplayName("Deve retornar false se usuário não for admin nem owner")
    void testIsNotAdminNorOwner() {
        UserModel user = new UserModel("user", "senha", UserRole.STANDARD);
        user.setId(1L);
        UserModel other = new UserModel("other", "senha", UserRole.STANDARD);
        other.setId(2L);
        TaskModel task = new TaskModel();
        task.setUser(other);
        assertFalse(service.isAdminOrOwner(user, task));
    }

    @Test
    @DisplayName("Deve retornar false se task não tiver usuário")
    void testTaskWithoutUser() {
        UserModel user = new UserModel("user", "senha", UserRole.STANDARD);
        user.setId(1L);
        TaskModel task = new TaskModel();
        task.setUser(null);
        assertFalse(service.isAdminOrOwner(user, task));
    }
}