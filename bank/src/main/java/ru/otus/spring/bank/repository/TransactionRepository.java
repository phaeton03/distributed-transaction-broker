package ru.otus.spring.bank.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.domain.Transaction;
import ru.otus.spring.bank.enums.Status;
import ru.otus.spring.bank.enums.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @EntityGraph(value = "client-account-graph")
    Optional<Transaction> findFirstByClientIdOrderByCreatedDtDesc(Long clientID);

    @EntityGraph(value = "client-account-graph")
    List<Transaction> findFirst10ByClientIdOrderByCreatedDtDesc(Long clientId);

    @EntityGraph(value = "client-account-graph")
    List<Transaction> findByCreatedDtBetweenAndClientId(LocalDate begin, LocalDate end, Long clientId);

    List<Transaction> findByAccountIdAndStatusInAndIsDistributedIsTrue(Long accountId, List<Status> status);

    @EntityGraph(value = "account-graph")
    List<Transaction> findByStatus(Status status);

    Optional<Transaction> findByTrackingNumberAndStatus(UUID trackingNumber, Status status);

    Boolean existsByTrackingNumber(UUID trackingNubmer);

    Optional<Transaction> findByTrackingNumber(UUID trackingNumber);

    void deleteByTrackingNumber(UUID trackingNumber);
}