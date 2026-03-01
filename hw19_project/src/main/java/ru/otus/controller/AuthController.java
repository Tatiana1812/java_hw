package ru.otus.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.otus.dto.request.AuthRequest;
import ru.otus.dto.response.AuthResponse;
import ru.otus.entity.Users;
import ru.otus.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Users> register(@Valid @RequestBody AuthRequest req) {
        Users user = authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody AuthRequest req, HttpServletRequest request) {
        authService.login(req, request);
    }

    @GetMapping("/profile")
    public AuthResponse profile(Authentication auth) {
        return authService.profile(auth);
    }
}