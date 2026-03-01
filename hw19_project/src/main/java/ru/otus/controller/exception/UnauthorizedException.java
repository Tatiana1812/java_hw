package ru.otus.controller.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("UNAUTHORIZED");
    }
}