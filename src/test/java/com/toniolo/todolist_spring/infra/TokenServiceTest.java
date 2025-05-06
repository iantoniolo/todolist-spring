package com.toniolo.todolist_spring.infra;

import com.toniolo.todolist_spring.model.UserModel;
import com.toniolo.todolist_spring.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "test-secret");
    }

    @Test
    @DisplayName("Deve gerar e validar token JWT corretamente")
    void testGenerateAndValidateToken() {
        UserModel user = new UserModel("usuario", "senha", UserRole.STANDARD);

        String token = tokenService.generateToken(user);
        assertNotNull(token);

        String subject = tokenService.validateToken(token);
        assertEquals("usuario", subject);
    }

    @Test
    @DisplayName("Deve retornar vazio para token inválido")
    void testValidateInvalidToken() {
        String invalidToken = "token-invalido";
        String subject = tokenService.validateToken(invalidToken);
        assertEquals("", subject);
    }
}