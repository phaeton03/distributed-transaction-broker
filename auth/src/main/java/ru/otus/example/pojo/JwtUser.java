package ru.otus.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class JwtUser implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private LocalDate lastPasswordReset;
    private boolean accountNonLocked;
    private Set<GrantedAuthority> authorities;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
