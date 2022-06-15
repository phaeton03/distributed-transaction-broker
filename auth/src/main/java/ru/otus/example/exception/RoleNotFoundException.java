package ru.otus.example.exception;

import ru.otus.example.exception.root.AuthBaseException;

public class RoleNotFoundException extends AuthBaseException {
    public RoleNotFoundException() {
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}
