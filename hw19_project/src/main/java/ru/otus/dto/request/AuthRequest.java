package ru.otus.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO запроса для регистрации/входа пользователя.
 *
 * <p>Пароль передаётся в открытом виде по HTTPS, на сервере хешируется (BCrypt) при регистрации.</p>
 *
 * @param login - логин пользователя
 * @param password - пароль пользователя
 */

public record AuthRequest(
    @NotBlank @Size(min=3,max=50) String login,
    @NotBlank @Size(min=6,max=100) String password
) {}