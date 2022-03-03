package ru.otus.spring.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.spring.bank.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}