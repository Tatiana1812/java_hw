package ru.otus.dto.response;

/**
 * DTO ответа с минимальными данными о текущем пользователе.
 *
 * @param id - идентификатор пользователя
 * @param login - логин пользователя
 */
public record AuthResponse(
        Long id,
        String login
) {}