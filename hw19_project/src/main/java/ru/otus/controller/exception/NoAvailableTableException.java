package ru.otus.controller.exception;

public class NoAvailableTableException extends RuntimeException {
    public NoAvailableTableException(String message) {
        super(message);
    }

    public NoAvailableTableException() {
        super("Нет доступных столиков на выбранное время");
    }
}
