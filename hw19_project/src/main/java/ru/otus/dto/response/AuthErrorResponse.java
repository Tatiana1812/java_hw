package ru.otus.dto.response;

/**
 * Унифицированный DTO ошибки для API.
 *
 * @param code - код ошибки
 * @param message - сообщение для отображения пользователю
 */
public record AuthErrorResponse(String code, String message) {}