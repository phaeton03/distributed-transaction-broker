package ru.otus.example.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.otus.example.entity.Role;
import ru.otus.example.entity.User;
import ru.otus.example.pojo.JwtUser;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(imports = LocalDate.class)
public interface JwtUserMapper {

    @Mapping(target = "enabled", constant = "true")
    @Mapping(target = "lastPasswordReset", expression = "java(LocalDate.now())")
    @Mapping(target = "authorities", source = "roles", qualifiedByName = "toGrantedAuthority")
    JwtUser toJwtUser(User user);

    @Named("toGrantedAuthority")
    default Set<GrantedAuthority> toGrantedAuthority(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
    }
}
