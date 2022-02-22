package ru.otus.spring.bank.service;

import ru.otus.spring.bank.controller.client.dto.request.TransactionRequest;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.domain.Transaction;
import ru.otus.spring.bank.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionService {
    List<Transaction> showLast10Transaction(Long clientId);

    List<Transaction> showTransactionsForPeriod(LocalDate start, LocalDate end, Long clientId);

    Transaction showLastTransaction(Long clientId);

    void recordLocalTransaction(BigDecimal amount, Long accountId, Client client, Long accountIdReceiver);

    Transaction recordGlobalTransaction(TransactionType type, BigDecimal amount,
                                        Long accountId, Client client, Long accountIdSender, UUID trackingNumber);

    Transaction recordLocalTransaction(TransactionType type, BigDecimal amount, Long accountId, Client client);

    void findTransactionByTrackingNumber(TransactionRequest transactionRequest);

    List<Transaction> calculateCommitTransactions();

    void findAllTransactionByTrackingNumber(List<TransactionRequest> transactionRequestList);

    void cancelTransaction(UUID trackingNumber);
}