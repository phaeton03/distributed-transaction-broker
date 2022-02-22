package com.example.financial_institution.repository;

import com.example.financial_institution.domain.Transaction;
import com.example.financial_institution.enums.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Boolean existsByTrackingNumber(UUID trackingNumber);

    @EntityGraph(value = "account-graph")
    List<Transaction> findByStatus(Status status);
}