package com.toniolo.todolist_spring.model;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("admin"),
    STANDARD("standard");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
