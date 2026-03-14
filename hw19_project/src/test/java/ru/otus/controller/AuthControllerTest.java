package ru.otus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.otus.controller.handler.GlobalExceptionHandler;
import ru.otus.dto.request.AuthRequest;
import ru.otus.dto.response.AuthResponse;
import ru.otus.entity.Users;
import ru.otus.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    AuthRequest request = new AuthRequest("user1", "pwd123");

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void registerShouldReturn201AndCreatedUser() throws Exception {
        Users saved = new Users();
        saved.setId(1L);
        saved.setLogin("user1");
        saved.setPassword("encoded");

        when(authService.register(any(AuthRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.login").value("user1"));

        ArgumentCaptor<AuthRequest> captor = ArgumentCaptor.forClass(AuthRequest.class);

        verify(authService).register(captor.capture());
        Assertions.assertEquals(saved.getLogin(), captor.getValue().login());
    }

    @Test
    void loginShouldDelegateToServiceAndReturn200() throws Exception {

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authService).login(any(AuthRequest.class), any(HttpServletRequest.class));
    }

    @Test
    void profileShouldReturnUserProfile() throws Exception {
        when(authService.profile(authentication)).thenReturn(new AuthResponse(10L, "user10"));

        mockMvc.perform(get("/api/auth/profile").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.login").value("user10"));

        verify(authService).profile(authentication);
    }
}
