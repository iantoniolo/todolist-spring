package com.toniolo.todolist_spring.infra;

import com.toniolo.todolist_spring.model.UserModel;
import com.toniolo.todolist_spring.model.UserRole;
import com.toniolo.todolist_spring.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

class SecurityFilterTest {

    private SecurityFilter securityFilter;
    private TokenService tokenService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        securityFilter = new SecurityFilter();
        tokenService = mock(TokenService.class);
        userRepository = mock(UserRepository.class);

        ReflectionTestUtils.setField(securityFilter, "tokenService", tokenService);
        ReflectionTestUtils.setField(securityFilter, "userRepository", userRepository);
    }

    @Test
    @DisplayName("Deve autenticar usuário quando token é válido")
    void testDoFilterInternalWithValidToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(tokenService.validateToken("valid-token")).thenReturn("usuario");
        UserDetails user = new UserModel("usuario", "senha", UserRole.STANDARD);
        when(userRepository.findByUsername("usuario")).thenReturn(user);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    @DisplayName("Deve seguir fluxo normalmente quando não há token")
    void testDoFilterInternalWithoutToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }
}