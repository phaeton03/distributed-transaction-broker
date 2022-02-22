package com.example.financial_institution.service;

import com.example.financial_institution.domain.Account;
import com.example.financial_institution.domain.Client;
import com.example.financial_institution.exception.AccountNotExistException;
import com.example.financial_institution.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImpTest {
    private final Long ACCOUNT_ID = 1L;

    private final Client CLIENT = new Client(1L, Collections.emptyList(), "NAME", "CITY");
    private final Account ACCOUNT = new Account(ACCOUNT_ID, "TYPE", BigDecimal.ZERO, "RUB", false, CLIENT);

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    ClientServiceImp clientService;

    @Test
    public void shouldThrowAccountNotExistException() {
        given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.showClient(ACCOUNT_ID)).isInstanceOf(AccountNotExistException.class);
    }

    @Test
    public void shouldReturnClient() {
        given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.of(ACCOUNT));

        assertThat(clientService.showClient(ACCOUNT_ID)).isEqualTo(CLIENT);
    }

}