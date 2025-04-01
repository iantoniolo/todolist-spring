package com.toniolo.todolist_spring.repository;

import com.toniolo.todolist_spring.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, Long> {
    List<TaskModel> findByUserId(Long userId);
}
