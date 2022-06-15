package ru.otus.example.controller.dto.request;

import lombok.Value;
import org.hibernate.validator.constraints.Length;
import ru.otus.example.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Value
public class UserRequest {
    @NotEmpty(message = "Please provide a user name")
    String username;

    @Email(message = "Please provide a valid email")
    String email;

    @NotEmpty(message = "Please provide a password")
    @Length(min = 5, message = "Your password must have at least 5 characters")
    String password;

}
