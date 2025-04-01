package com.toniolo.todolist_spring.repository;

import com.toniolo.todolist_spring.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {

    UserDetails findByUsername(String username);
}
