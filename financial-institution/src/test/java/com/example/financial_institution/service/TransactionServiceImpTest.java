package com.example.financial_institution.service;

import com.example.financial_institution.domain.Account;
import com.example.financial_institution.domain.Transaction;
import com.example.financial_institution.enums.Status;
import com.example.financial_institution.enums.TransactionType;
import com.example.financial_institution.mapper.TransactionMapper;
import com.example.financial_institution.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImpTest {
    private final Account ACCOUNT_ONE = new Account(1L, "TYPE", BigDecimal.TEN, "RUB", false, null);

    private final Transaction TRANSACTION_ONE
            = Transaction.builder()
            .status(Status.COMMIT)
            .account(ACCOUNT_ONE)
            .amount(BigDecimal.valueOf(100))
            .build();

    private final Transaction TRANSACTION_TWO
            = Transaction.builder()
            .status(Status.COMMIT)
            .account(ACCOUNT_ONE)
            .amount(BigDecimal.valueOf(110))
            .build();

    private final BigDecimal CHECK_SUM = BigDecimal.valueOf(220);

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImp transactionService;

    @Test
    public void shouldCalculateCorrectCommitTransactions() {
        final List<Transaction> transactions = List.of(TRANSACTION_ONE, TRANSACTION_TWO);

        given(transactionRepository.findByStatus(Status.COMMIT)).willReturn(transactions);
        transactionService.calculateCommitTransactions();

        assertThat(transactions).extracting(transaction -> transaction.getAccount().getAmount()).contains(CHECK_SUM);
    }

    @Test
    public void shouldSetStatusSUCCESSCommitTransactions() {
        final List<Transaction> transactions = List.of(TRANSACTION_ONE, TRANSACTION_TWO);

        given(transactionRepository.findByStatus(Status.COMMIT)).willReturn(transactions);
        transactionService.calculateCommitTransactions();

        assertThat(transactions).extracting(transaction -> transaction.getStatus()).contains(Status.SUCCESS);
    }
}