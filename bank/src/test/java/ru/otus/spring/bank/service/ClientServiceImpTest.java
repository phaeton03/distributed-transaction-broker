package ru.otus.spring.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.bank.controller.client.CorrespodentBankClient;
import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.exception.ClientNotFoundException;
import ru.otus.spring.bank.repository.ClientRepository;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImpTest {
    private final Long CLIENT_ID = 1L;

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

    private final Client CLIENT = new Client(CLIENT_ID,
            List.of(ACCOUNT, ACCOUNT_RECEIVER, ACCOUNT_RECEIVER_BLOCKED), "NAME", "CITY", Collections.emptyList());

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CorrespodentBankClient correspodentBankClient;

    @InjectMocks
    ClientServiceImp clientService;

    public List<Account> showAllAccounts(Long clientId) {

        return clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new).getAccounts();
    }

    @Test
    public void shouldTrowClientNotFoundException() {
        given(clientRepository.findById(CLIENT_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.showAllAccounts(CLIENT_ID)).isInstanceOf(ClientNotFoundException.class);
    }

    @Test
    public void shouldReturnCorrectListByShowAllAccounts() {
        given(clientRepository.findById(CLIENT_ID)).willReturn(Optional.of(CLIENT));

        assertThat(clientService.showAllAccounts(CLIENT_ID)).hasSize(3).first().isInstanceOf(Account.class);
    }
}