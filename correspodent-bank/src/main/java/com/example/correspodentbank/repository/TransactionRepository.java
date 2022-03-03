package com.example.correspodentbank.repository;

import com.example.correspodentbank.domain.Transaction;
import com.example.correspodentbank.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Modifying
    @Query("update Transaction t set t.bankStatus = :status where t.trackingNumber = :trackingNumber")
    void updateTransactionByBankStatusAndTrackingNumber(Status status, UUID trackingNumber);

    void deleteByTrackingNumberAndBankStatus(UUID trackingNumber, Status status);

    List<Transaction> getAllByBankStatus(Status status);

    List<Transaction> getAllByBankStatusAndFinancialInstitutionStatusIsNot(Status bankStatus, Status financialIntermediaryStatus);
}