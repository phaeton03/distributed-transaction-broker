package ru.otus.spring.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.bank.domain.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByIdAndClientId(Long id, Long clientId);
}