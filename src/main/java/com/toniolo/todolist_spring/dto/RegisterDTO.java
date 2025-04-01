package com.toniolo.todolist_spring.dto;

import com.toniolo.todolist_spring.model.UserRole;

public record RegisterDTO(String username, String password, UserRole role) {
}
