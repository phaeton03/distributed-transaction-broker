package com.example.correspodentbank.service;

import com.example.correspodentbank.controller.client.BankClient;
import com.example.correspodentbank.controller.client.FinancialIntermediaryClient;
import com.example.correspodentbank.controller.dto.request.TransactionRequest;
import com.example.correspodentbank.domain.Transaction;
import com.example.correspodentbank.enums.Status;
import com.example.correspodentbank.mapper.TransactionMapper;
import com.example.correspodentbank.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImpTest {

    private final Transaction TRANSACTION_COMMIT_ONE = Transaction.builder()
            .bankStatus(Status.COMMIT)
            .amount(BigDecimal.valueOf(100))
            .build();

    private final Transaction TRANSACTION_COMMIT_TWO = Transaction.builder()
            .bankStatus(Status.COMMIT)
            .amount(BigDecimal.valueOf(300))
            .build();

    private final Transaction TRANSACTION_COMMIT_THREE = Transaction.builder()
            .bankStatus(Status.COMMIT)
            .amount(BigDecimal.valueOf(300))
            .build();

    private final Transaction TRANSACTION_SUCCESS_ONE = Transaction.builder()
            .accountSenderId(1L)
            .accountSenderId(1L)
            .trackingNumber(UUID.randomUUID())
            .bankStatus(Status.SUCCESS)
            .financialInstitutionStatus(Status.COMMIT)
            .amount(BigDecimal.valueOf(100))
            .build();

    private final Transaction TRANSACTION_SUCCESS_TWO = Transaction.builder()
            .accountSenderId(1L)
            .accountSenderId(1L)
            .trackingNumber(UUID.randomUUID())
            .bankStatus(Status.SUCCESS)
            .amount(BigDecimal.valueOf(300))
            .build();

    private final Transaction TRANSACTION_SUCCESS_THREE = Transaction.builder()
            .accountSenderId(1L)
            .accountSenderId(1L)
            .trackingNumber(UUID.randomUUID())
            .bankStatus(Status.SUCCESS)
            .amount(BigDecimal.valueOf(300))
            .build();

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BankClient bankClient;

    @Mock
    private FinancialIntermediaryClient financialIntermediaryClient;

    @Mock
    private TransactionMapper transactionMapper = new TransactionMapper();

    @InjectMocks
    private TransactionServiceImp transactionService;

    @Test
    public void shouldSetBankSUCCESSStatusByCalculateBankCommitTransactions() {
        final List<Transaction> transactions = List.of(TRANSACTION_COMMIT_ONE, TRANSACTION_COMMIT_TWO, TRANSACTION_COMMIT_THREE);

        given(transactionRepository.getAllByBankStatus(Status.COMMIT)).willReturn(transactions);
        given(transactionRepository.saveAll(transactions)).willReturn(transactions);

        assertThat(transactionService.calculateBankCommitTransactions())
                .hasSize(3)
                .extracting(Transaction::getBankStatus)
                .contains(Status.SUCCESS, Status.SUCCESS, Status.SUCCESS);
    }

    @Test
    public void shouldSetInstitutionSUCCESSStatusByCalculateBankCommitTransactions() {

        final List<Transaction> transactions = List.of(TRANSACTION_SUCCESS_ONE, TRANSACTION_SUCCESS_TWO, TRANSACTION_SUCCESS_THREE);

        given(transactionRepository
                .getAllByBankStatusAndFinancialInstitutionStatusIsNot(Status.SUCCESS, Status.SUCCESS))
                .willReturn(transactions);
        given(financialIntermediaryClient.existTransaction(any())).willReturn(false);
        given(transactionRepository.saveAll(transactions)).willReturn(transactions);

        assertThat(transactionService.calculateSuccessBankTransactionsToFinancialInstitution())
                .hasSize(3)
                .extracting(Transaction::getFinancialInstitutionStatus)
                .contains(Status.SUCCESS, Status.SUCCESS, Status.SUCCESS);
    }

    @Test
    public void shouldRemainInstitutionStatusWithoutChangeByCalculateBankCommitTransactions() {

        final List<Transaction> transactions = List.of(TRANSACTION_SUCCESS_ONE, TRANSACTION_SUCCESS_TWO, TRANSACTION_SUCCESS_THREE);

        given(transactionRepository
                .getAllByBankStatusAndFinancialInstitutionStatusIsNot(Status.SUCCESS, Status.SUCCESS))
                .willReturn(transactions);
        given(financialIntermediaryClient.existTransaction(any())).willReturn(true);

        assertThat(transactionService.calculateSuccessBankTransactionsToFinancialInstitution())
                .hasSize(3)
                .extracting(Transaction::getFinancialInstitutionStatus)
                .doesNotContain(Status.SUCCESS, Status.SUCCESS, Status.SUCCESS);
    }

}