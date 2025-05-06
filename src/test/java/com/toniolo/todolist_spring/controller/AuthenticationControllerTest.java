package com.toniolo.todolist_spring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toniolo.todolist_spring.dto.AuthenticationDTO;
import com.toniolo.todolist_spring.dto.RegisterDTO;
import com.toniolo.todolist_spring.infra.TokenService;
import com.toniolo.todolist_spring.model.UserModel;
import com.toniolo.todolist_spring.repository.UserRepository;
import com.toniolo.todolist_spring.model.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenService tokenService;

    @Test
    @DisplayName("Deve fazer login com sucesso e retornar token")
    void testLoginSuccess() throws Exception {
        AuthenticationDTO loginDto = new AuthenticationDTO("usuario", "senha");
        UserModel user = new UserModel("usuario", "senhaCriptografada", UserRole.STANDARD);
        String token = "token-jwt-fake";

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null);

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(tokenService.generateToken(any(UserModel.class))).thenReturn(token);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    @DisplayName("Deve registrar novo usuário com sucesso")
    void testRegisterSuccess() throws Exception {
        RegisterDTO registerDto = new RegisterDTO("novoUsuario", "senha", UserRole.STANDARD);

        Mockito.when(userRepository.findByUsername("novoUsuario")).thenReturn(null);
        Mockito.when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar registrar usuário já existente")
    void testRegisterUserAlreadyExists() throws Exception {
        RegisterDTO registerDto = new RegisterDTO("usuarioExistente", "senha", UserRole.STANDARD);
        UserModel existingUser = new UserModel("usuarioExistente", "senhaCriptografada", UserRole.STANDARD);

        Mockito.when(userRepository.findByUsername("usuarioExistente")).thenReturn(existingUser);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());
    }
}