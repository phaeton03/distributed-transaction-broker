package ru.otus.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.example.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(String name);
}
