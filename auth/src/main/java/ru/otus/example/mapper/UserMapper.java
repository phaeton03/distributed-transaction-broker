package ru.otus.example.mapper;

import lombok.RequiredArgsConstructor;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.example.controller.dto.request.UserRequest;
import ru.otus.example.entity.Role;
import ru.otus.example.entity.User;
import ru.otus.example.repository.RoleRepository;
import ru.otus.example.service.RoleService;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserMapper {
    @Autowired
    protected RoleService roleService;

    @Mapping(target = "roles", expression = "java( roleService.findDefaultRole())")
    abstract public User toUserEntity(UserRequest userRequest);

}
