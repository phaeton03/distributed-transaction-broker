package ru.otus.example.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.example.controller.dto.request.UserRequest;
import ru.otus.example.entity.User;
import ru.otus.example.mapper.UserMapper;
import ru.otus.example.repository.UserRepository;
import ru.otus.example.service.UserService;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User saveUser(UserRequest userRequest) {
        log.info("save a user");
        User user = userMapper.toUserEntity(userRequest);

        return userRepository.save(user);
    }

    @Override
    public void login(String login, String password) {

    }

    @Override
    public void recoveryPassword(String login) {

    }
}
