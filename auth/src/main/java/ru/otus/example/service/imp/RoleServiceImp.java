package ru.otus.example.service.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.example.entity.Role;
import ru.otus.example.exception.RoleNotFoundException;
import ru.otus.example.repository.RoleRepository;
import ru.otus.example.service.RoleService;

import javax.xml.bind.annotation.XmlType;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImp implements RoleService {
    private final String DEFAULT_ROLE = "USER";
    private final RoleRepository roleRepository;

    @Override
    public Set<Role> findDefaultRole() {
        log.info("find default role");

        return Set.of(roleRepository.findRoleByName(DEFAULT_ROLE)
                .orElseThrow(() ->
                        new RoleNotFoundException(String.format("DEFAULT ROLE not found with name = %s", DEFAULT_ROLE))));
    }
}
