package ru.otus.example.exception;

import ru.otus.example.exception.root.AuthBaseException;

public class UserNotFoundException extends AuthBaseException {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
