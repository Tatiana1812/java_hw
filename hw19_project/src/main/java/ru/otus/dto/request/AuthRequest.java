package ru.otus.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(
    @NotBlank @Size(min=3,max=50) String login,
    @NotBlank @Size(min=6,max=100) String password
) {}