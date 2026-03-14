package ru.otus.controller.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import ru.otus.controller.exception.UnauthorizedException;
import ru.otus.dto.response.AuthErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<AuthErrorResponse> handleLoginExists(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new AuthErrorResponse("LOGIN_EXISTS", "Логин уже занят"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<AuthErrorResponse> handleInvalidCredentials(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthErrorResponse("INVALID_CREDENTIALS", "Неверный логин или пароль"));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<AuthErrorResponse> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthErrorResponse("UNAUTHORIZED", "Требуется авторизация"));
    }
}