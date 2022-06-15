package ru.otus.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.example.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUserName(String userName);
}
