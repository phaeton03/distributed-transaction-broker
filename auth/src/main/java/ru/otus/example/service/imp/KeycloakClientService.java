package ru.otus.example.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.example.config.KeycloakProvider;
import ru.otus.example.controller.dto.request.UserRequest;
import ru.otus.example.entity.User;
import ru.otus.example.mapper.UserMapper;
import ru.otus.example.service.UserService;

import javax.ws.rs.core.Response;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakClientService {
    @Value("${keycloak.realm}")
    public String realm;
    private final KeycloakProvider keycloakProvider;
    private final UserService userService;

    public Response createKeycloakUser(UserRequest user) {
        log.info("start create Keycloak user");

        UsersResource usersResource = keycloakProvider.getInstance().realm(realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(user.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(user.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setEmail(user.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        Response response = usersResource.create(kcUser);

        if (response.getStatus() == 201) {
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            usersResource.get(userId).sendVerifyEmail();
            userService.saveUser(user);
        }

        return response;

    }

    private static CredentialRepresentation createPasswordCredentials(String password) {
        log.info("create password credentials");

        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
