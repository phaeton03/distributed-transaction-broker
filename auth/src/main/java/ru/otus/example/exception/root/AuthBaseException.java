package ru.otus.example.exception.root;

public class AuthBaseException extends RuntimeException {
    public AuthBaseException() {
    }

    public AuthBaseException(String message) {
        super(message);
    }
}
