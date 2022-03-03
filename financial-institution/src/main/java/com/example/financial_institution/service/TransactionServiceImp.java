package com.example.financial_institution.service;

import com.example.financial_institution.controller.dto.request.TransactionRequest;
import com.example.financial_institution.domain.Transaction;
import com.example.financial_institution.enums.Status;
import com.example.financial_institution.mapper.TransactionMapper;
import com.example.financial_institution.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional(readOnly = true)
    public Boolean existTransactionByTrackingNumber(UUID trackingNumber) {
        return transactionRepository.existsByTrackingNumber(trackingNumber);
    }

    @Override
    public void startTransaction(List<TransactionRequest> transactionRequests) {
        final List<Transaction> transactions =
                transactionRequests.stream().map(transactionMapper::mapToFinanciaInstitutionTransaction).collect(Collectors.toList());

        transactionRepository.saveAll(transactions);
    }

    @Override
    @Transactional()
    public void calculateCommitTransactions() {
        final List<Transaction> commitTransactions = transactionRepository.findByStatus(Status.COMMIT);

        if (!commitTransactions.isEmpty()) {
            for (Transaction tx : commitTransactions) {
                final BigDecimal add = tx.getAccount().getAmount().add(tx.getAmount());
                tx.getAccount().setAmount(add);
            }
        }

        commitTransactions.forEach(transaction -> transaction.setStatus(Status.SUCCESS));
        transactionRepository.saveAll(commitTransactions);
    }
}