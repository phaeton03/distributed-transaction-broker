package ru.otus.example.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.example.entity.User;
import ru.otus.example.exception.UserNotFoundException;
import ru.otus.example.mapper.JwtUserMapper;
import ru.otus.example.service.UserService;

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;
    private final JwtUserMapper jwtUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user from database.....");

        User user = userService.findUserByEmail(username)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format("User with userName = %s not found", username)));

        log.info("User with username = {} was successfully loaded", username);

        return jwtUserMapper.toJwtUser(user);
    }
}
