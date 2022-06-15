package ru.otus.example.service;

import ru.otus.example.controller.dto.request.UserRequest;
import ru.otus.example.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByUserName(String userName);

    void login(String login, String password);

    void recoveryPassword(String login);

    User saveUser(UserRequest userRequest);
}
