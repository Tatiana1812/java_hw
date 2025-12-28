package ru.otus.appcontainer.exceptions;

public class DuplicateComponentException extends RuntimeException{
    public DuplicateComponentException(String message) {
        super(message);
    }
}
