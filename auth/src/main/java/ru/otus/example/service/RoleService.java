package ru.otus.example.service;

import ru.otus.example.entity.Role;

import java.util.Set;

public interface RoleService {
    Set<Role> findDefaultRole();
}
