package ru.otus.spring.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.bank.controller.client.FinancialInstitutionClient;
import ru.otus.spring.bank.controller.dto.repsonse.AccountResponse;
import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.domain.Transaction;
import ru.otus.spring.bank.enums.Status;
import ru.otus.spring.bank.exception.*;
import ru.otus.spring.bank.repository.AccountRepository;
import ru.otus.spring.bank.repository.ClientRepository;
import ru.otus.spring.bank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImpTest {
    private final Client CLIENT_EMPTY_ACCOUNTS = new Client(1L, Collections.emptyList(), "NAME", "CITY", Collections.emptyList());

    private final Transaction TRANSACTION_ONE = Transaction.builder()
            .isDistributed(true)
            .status(Status.COMMIT)
            .amount(BigDecimal.valueOf(100))
            .build();

    private final Transaction TRANSACTION_TWO = Transaction.builder()
            .isDistributed(true)
            .status(Status.COMMIT)
            .amount(BigDecimal.valueOf(300))
            .build();

    private final Transaction TRANSACTION_THREE = Transaction.builder()
            .isDistributed(true)
            .status(Status.COMMIT)
            .amount(BigDecimal.valueOf(300))
            .build();

    private final Account ACCOUNT_BLOCKED = Account.builder()
            .amount(BigDecimal.ZERO)
            .isBlock(true)
            .build();

    private final Account ACCOUNT_INSUFFICIENT_AMOUNT = Account.builder()
            .id(1L)
            .amount(BigDecimal.ZERO)
            .isBlock(false)
            .build();

    private final Account ACCOUNT = Account.builder()
            .id(1L)
            .amount(BigDecimal.TEN)
            .isBlock(false)
            .build();

    private final Account ACCOUNT_RECEIVER = Account.builder()
            .id(1L)
            .amount(BigDecimal.ZERO)
            .isBlock(false)
            .build();

    private final Account ACCOUNT_RECEIVER_BLOCKED = Account.builder()
            .id(1L)
            .amount(BigDecimal.ZERO)
            .isBlock(true)
            .build();

    private final AccountResponse ACCOUNT_RESPONSE = AccountResponse.builder()
            .id(ACCOUNT_RECEIVER.getId())
            .amount(ACCOUNT_RECEIVER.getAmount())
            .isBlock(ACCOUNT_RECEIVER.getIsBlock())
            .build();

    private final AccountResponse ACCOUNT_RESPONSE_BLOCKED = AccountResponse.builder()
            .id(ACCOUNT_RECEIVER_BLOCKED.getId())
            .amount(ACCOUNT_RECEIVER_BLOCKED.getAmount())
            .isBlock(ACCOUNT_RECEIVER_BLOCKED.getIsBlock())
            .build();

    private final Client CLIENT = new Client(1L, List.of(ACCOUNT, ACCOUNT, ACCOUNT_BLOCKED), "NAME", "CITY", Collections.emptyList());

    private final BigDecimal ADD_TEN = BigDecimal.TEN;

    private final BigDecimal NEGATIVE_AMOUNT = BigDecimal.valueOf(-100);

    private final BigDecimal RESERVE_AMOUNT = BigDecimal.valueOf(100);
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountServiceImp self;

    @Mock
    private FinancialInstitutionClient financialInstitutionClient;

    @InjectMocks
    private AccountServiceImp accountService;

    @Test
    public void shouldThrowAccountBlockException() {
        assertThatThrownBy(() -> accountService.deposit(ACCOUNT_BLOCKED, BigDecimal.ZERO)).isInstanceOf(AccountBlockException.class);
    }

    @Test
    public void shouldAddCorrectAmount() {
        BigDecimal checkDeposit = ACCOUNT.getAmount().add(ADD_TEN);
        accountService.deposit(ACCOUNT, ADD_TEN);

        assertThat(ACCOUNT.getAmount().compareTo(checkDeposit)).isEqualTo(0);
    }

    @Test
    public void shouldWithdrawalThrowAccountBlockException() {
        assertThatThrownBy(() -> accountService.withdrawal(ACCOUNT_BLOCKED, BigDecimal.ZERO)).isInstanceOf(AccountBlockException.class);
    }

    @Test
    public void shouldWithdrawalThrowNegativeAmountException() {
        assertThatThrownBy(() -> accountService.withdrawal(ACCOUNT, NEGATIVE_AMOUNT)).isInstanceOf(NegativeAmountException.class);
    }

    @Test
    public void shouldWithdrawalThrowInsufficientMoneyException() {
        given(self.getReserveMoney(ACCOUNT.getId())).willReturn(BigDecimal.ZERO);

        assertThatThrownBy(() -> accountService.withdrawal(ACCOUNT_INSUFFICIENT_AMOUNT, ADD_TEN)).isInstanceOf(InsufficientMoneyException.class);
    }

    @Test
    public void shouldWithdrawalCorrectAmount() {

        given(self.getReserveMoney(ACCOUNT.getId())).willReturn(BigDecimal.ZERO);
        accountService.withdrawal(ACCOUNT, ADD_TEN);

        assertThat(ACCOUNT.getAmount().compareTo(BigDecimal.ZERO)).isEqualTo(0);
    }


    @Test
    public void shouldReturnCorrectSizeAllAccounts() {
        given(accountRepository.findAll()).willReturn(List.of(ACCOUNT, ACCOUNT_BLOCKED));

        assertThat(accountService.showAllAccounts()).hasSize(2).contains(ACCOUNT_BLOCKED);
    }

    @Test
    public void shouldThowAccountNotExistException() {
        given(accountRepository.findById(ACCOUNT.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.showAccount(ACCOUNT.getId())).isInstanceOf(AccountNotExistException.class);
    }

    @Test
    public void shouldReturnAccountByShowAccouunt() {
        given(accountRepository.findById(ACCOUNT.getId())).willReturn(Optional.of(ACCOUNT));

        assertThat(accountService.showAccount(ACCOUNT.getId())).isEqualTo(ACCOUNT);
    }

    @Test
    public void shouldThrowClientNotFoundExceptionByShowClientAccount() {
        given(clientRepository.findById(CLIENT_EMPTY_ACCOUNTS.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.showClientAccount(ACCOUNT.getId(), CLIENT_EMPTY_ACCOUNTS.getId())).isInstanceOf(ClientNotFoundException.class);
    }

    @Test
    public void shouldThrowAccountNotExistExceptionByShowClientAccount() {
        given(clientRepository.findById(CLIENT_EMPTY_ACCOUNTS.getId())).willReturn(Optional.of(CLIENT_EMPTY_ACCOUNTS));

        assertThatThrownBy(() -> accountService.showClientAccount(ACCOUNT.getId(), CLIENT_EMPTY_ACCOUNTS.getId())).isInstanceOf(AccountNotExistException.class);
    }

    @Test
    public void shouldReturnCorrectAccount() {
        given(clientRepository.findById(CLIENT.getId())).willReturn(Optional.of(CLIENT));

        assertThat(accountService.showClientAccount(ACCOUNT.getId(), CLIENT.getId())).isEqualTo(ACCOUNT);
    }

    @Test
    public void shouldThrowAccountBlockExceptionByCheckAccounts() {
        given(financialInstitutionClient.getAccount(ACCOUNT_RECEIVER.getId())).willReturn(ACCOUNT_RESPONSE);
        given(accountRepository.findById(ACCOUNT_BLOCKED.getId())).willReturn(Optional.of(ACCOUNT_BLOCKED));

        assertThatThrownBy(() -> accountService.checkAccounts(ACCOUNT_BLOCKED.getId(), ACCOUNT_RECEIVER.getId(), ADD_TEN))
                .isInstanceOf(AccountBlockException.class);
    }

    @Test
    public void shouldThrowAccountBlockExceptionByCheckAccountsIfAccountReceiverIsBlocked() {
        given(financialInstitutionClient.getAccount(ACCOUNT_RECEIVER_BLOCKED.getId())).willReturn(ACCOUNT_RESPONSE_BLOCKED);
        given(accountRepository.findById(ACCOUNT.getId())).willReturn(Optional.of(ACCOUNT));

        assertThatThrownBy(() -> accountService.checkAccounts(ACCOUNT.getId(), ACCOUNT_RECEIVER_BLOCKED.getId(), ADD_TEN))
                .isInstanceOf(AccountBlockException.class);
    }

    @Test
    public void shouldThrowInsufficientMoneyExceptionByCheckAccounts() {
        given(financialInstitutionClient.getAccount(ACCOUNT_RECEIVER.getId())).willReturn(ACCOUNT_RESPONSE);
        given(accountRepository.findById(ACCOUNT.getId())).willReturn(Optional.of(ACCOUNT));
        given(self.getReserveMoney(ACCOUNT.getId())).willReturn(RESERVE_AMOUNT);

        assertThatThrownBy(() -> accountService.checkAccounts(ACCOUNT.getId(), ACCOUNT_RECEIVER.getId(), ADD_TEN))
                .isInstanceOf(InsufficientMoneyException.class);
    }

    @Test
    public void shouldThrowNegativeAmountExceptionByCheckAccounts() {
        given(financialInstitutionClient.getAccount(ACCOUNT_RECEIVER.getId())).willReturn(ACCOUNT_RESPONSE);
        given(accountRepository.findById(ACCOUNT.getId())).willReturn(Optional.of(ACCOUNT));
        given(self.getReserveMoney(ACCOUNT.getId())).willReturn(BigDecimal.ZERO);

        assertThatThrownBy(() -> accountService.checkAccounts(ACCOUNT.getId(), ACCOUNT_RECEIVER.getId(), NEGATIVE_AMOUNT))
                .isInstanceOf(NegativeAmountException.class);
    }

    @Test
    public void shouldReturnTrueByCheckAccounts() {
        given(financialInstitutionClient.getAccount(ACCOUNT_RECEIVER.getId())).willReturn(ACCOUNT_RESPONSE);
        given(accountRepository.findById(ACCOUNT.getId())).willReturn(Optional.of(ACCOUNT));
        given(self.getReserveMoney(ACCOUNT.getId())).willReturn(BigDecimal.ZERO);

        assertThat(accountService.checkAccounts(ACCOUNT.getId(), ACCOUNT_RECEIVER.getId(), ADD_TEN)).isTrue();
    }

    @Test
    public void shouldReturnCorrectSum() {
        given(transactionRepository.findByAccountIdAndStatusInAndIsDistributedIsTrue(ACCOUNT.getId(),List.of(Status.IN_PROGRESS, Status.COMMIT)))
                .willReturn(List.of(TRANSACTION_ONE, TRANSACTION_TWO, TRANSACTION_THREE));

        BigDecimal checkSum = TRANSACTION_ONE.getAmount().add(TRANSACTION_TWO.getAmount()).add(TRANSACTION_THREE.getAmount());

        assertThat(accountService.getReserveMoney(ACCOUNT.getId()).compareTo(checkSum)).isEqualTo(0);
    }
}