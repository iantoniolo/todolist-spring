package com.toniolo.todolist_spring.service;

import com.toniolo.todolist_spring.model.UserModel;
import com.toniolo.todolist_spring.model.UserRole;
import com.toniolo.todolist_spring.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorizationServiceTest {

    @Test
    @DisplayName("Deve retornar usuário quando encontrado")
    void testLoadUserByUsernameFound() {
        UserRepository userRepository = mock(UserRepository.class);
        AuthorizationService service = new AuthorizationService();
        ReflectionTestUtils.setField(service, "repository", userRepository);

        UserModel user = new UserModel("usuario", "senha", UserRole.STANDARD);
        when(userRepository.findByUsername("usuario")).thenReturn(user);

        assertEquals(user, service.loadUserByUsername("usuario"));
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando usuário não encontrado")
    void testLoadUserByUsernameNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        AuthorizationService service = new AuthorizationService();
        ReflectionTestUtils.setField(service, "repository", userRepository);

        when(userRepository.findByUsername("naoexiste")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("naoexiste"));
    }
}