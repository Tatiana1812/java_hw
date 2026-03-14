package ru.otus.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import ru.otus.dto.request.AuthRequest;
import ru.otus.dto.response.AuthResponse;
import ru.otus.entity.Users;

public interface AuthService {
    Users register(AuthRequest req);

    void login(AuthRequest req, HttpServletRequest request);

    AuthResponse profile(Authentication auth);
}
