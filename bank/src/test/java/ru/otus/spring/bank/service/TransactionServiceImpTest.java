package ru.otus.spring.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.bank.controller.client.CorrespodentBankClient;
import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Transaction;
import ru.otus.spring.bank.enums.Status;
import ru.otus.spring.bank.enums.TransactionType;
import ru.otus.spring.bank.exception.AccountNotExistException;
import ru.otus.spring.bank.exception.IncorrectValueAmountException;
import ru.otus.spring.bank.exception.NotFoundTransactionException;
import ru.otus.spring.bank.repository.AccountRepository;
import ru.otus.spring.bank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImpTest {
    private final Long CLIENT_ID = 1L;
    private final Long ACCOUNT_ID = 1L;
    private final Long ACCOUNT_RECEIVER_ID = 1L;

    private final Account ACCOUNT_ONE = Account.builder()
            .id(1L)
            .amount(BigDecimal.TEN)
            .isBlock(false)
            .build();

    private final BigDecimal SUBTRACT_RESULT_ONE = BigDecimal.TEN;

    private final Account ACCOUNT_TWO = Account.builder()
            .id(1L)
            .amount(BigDecimal.valueOf(100))
            .isBlock(false)
            .build();

    private final BigDecimal SUBTRACT_RESULT_TWO = BigDecimal.valueOf(70);

    private final Account ACCOUNT_THREE = Account.builder()
            .id(1L)
            .amount(BigDecimal.valueOf(1000))
            .isBlock(false)
            .build();

    private final BigDecimal SUBTRACT_RESULT_THREE = BigDecimal.valueOf(700);

    private final Transaction TRANSACTION_ONE = Transaction.builder()
            .status(Status.IN_PROGRESS)
            .type(TransactionType.DEBIT)
            .amount(BigDecimal.ZERO)
            .account(ACCOUNT_ONE)
            .client(null)
            .isDistributed(false)
            .build();

    private final Transaction TRANSACTION_TWO = Transaction.builder()
            .isDistributed(true)
            .status(Status.COMMIT)
            .amount(BigDecimal.valueOf(30))
            .account(ACCOUNT_TWO)
            .build();

    private final Transaction TRANSACTION_THREE = Transaction.builder()
            .isDistributed(true)
            .status(Status.COMMIT)
            .amount(BigDecimal.valueOf(300))
            .account(ACCOUNT_THREE)
            .build();

    private final Transaction TRANSACTION_AMOUNT_EXCEPTION = Transaction.builder()
            .isDistributed(true)
            .status(Status.COMMIT)
            .amount(BigDecimal.valueOf(30000))
            .account(ACCOUNT_THREE)
            .build();

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CorrespodentBankClient correspodentBankClient;

    @InjectMocks
    TransactionServiceImp transactionService;

    @Test
    public void shouldThrowAccountNotExistException() {
        given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.recordLocalTransaction(TransactionType.DEBIT, BigDecimal.ZERO, ACCOUNT_ID, null))
                .isInstanceOf(AccountNotExistException.class);
    }

    @Test
    public void shouldReturnCorrectTransaction() {
        given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.of(ACCOUNT_ONE));
        given(transactionRepository.save(any())).willReturn(TRANSACTION_ONE);

        assertThat(transactionService.recordLocalTransaction(TransactionType.DEBIT, BigDecimal.ZERO, ACCOUNT_ID, null))
                .isEqualTo(TRANSACTION_ONE);
    }

    @Test
    public void shouldThrowAccountNotExistExceptionByRecordGlobalTransaction() {
        given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService
                .recordGlobalTransaction(TransactionType.DEBIT, BigDecimal.ZERO, ACCOUNT_ID, null, ACCOUNT_RECEIVER_ID, UUID.randomUUID()))
                .isInstanceOf(AccountNotExistException.class);
    }

    @Test
    public void shouldThrowNotFoundTransactionException() {
        given(transactionRepository.findFirstByClientIdOrderByCreatedDtDesc(CLIENT_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.showLastTransaction(CLIENT_ID)).isInstanceOf(NotFoundTransactionException.class);
    }

    @Test
    public void shouldCalculateCorrectCommitTransactions() {
        final List<Transaction> transactions = List.of(TRANSACTION_ONE, TRANSACTION_TWO, TRANSACTION_THREE);

        given(transactionRepository.findByStatus(Status.COMMIT)).willReturn(transactions);
        given(transactionRepository.saveAll(transactions)).willReturn(transactions);

        assertThat(transactionService.calculateCommitTransactions())
                .hasSize(3)
                .extracting(tx -> tx.getAccount().getAmount())
                .contains(SUBTRACT_RESULT_ONE, SUBTRACT_RESULT_TWO, SUBTRACT_RESULT_THREE);
    }

    @Test
    public void shouldSetSUCCESSStatusByCalculateCorrectCommitTransactions() {
        final List<Transaction> transactions = List.of(TRANSACTION_ONE, TRANSACTION_TWO, TRANSACTION_THREE);

        given(transactionRepository.findByStatus(Status.COMMIT)).willReturn(transactions);
        given(transactionRepository.saveAll(transactions)).willReturn(transactions);

        assertThat(transactionService.calculateCommitTransactions())
                .hasSize(3)
                .extracting(tx -> tx.getStatus())
                .contains(Status.SUCCESS, Status.SUCCESS, Status.SUCCESS);
    }

    @Test
    public void shouldThrowIncorrectValueAmountExceptionByCalculateCorrectCommitTransactions() {
        final List<Transaction> transactions = List.of(TRANSACTION_ONE, TRANSACTION_TWO, TRANSACTION_AMOUNT_EXCEPTION);

        given(transactionRepository.findByStatus(Status.COMMIT)).willReturn(transactions);

        assertThatThrownBy(() -> transactionService.calculateCommitTransactions()).isInstanceOf(IncorrectValueAmountException.class);
    }
}