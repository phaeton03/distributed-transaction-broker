package ru.otus.spring.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Client;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BankServiceImpTest {

    private final Long ACCOUNT_ID = 1L;

    private final Client CLIENT_EMPTY_ACCOUNTS =
            new Client(1L,Collections.emptyList(), "NAME", "CITY", Collections.emptyList());

    private final Account ACCOUNT = Account.builder()
            .id(1L)
            .amount(BigDecimal.ZERO)
            .isBlock(false)
            .client(CLIENT_EMPTY_ACCOUNTS)
            .build();

    @Mock
    private AccountService accountService;

    @InjectMocks
    private BankServiceImp bankService;

    @Test
    public void shouldGetCorrectClient() {
        given(accountService.showAccount(ACCOUNT_ID)).willReturn(ACCOUNT);

        assertThat(bankService.showClient(ACCOUNT_ID)).isEqualTo(CLIENT_EMPTY_ACCOUNTS);
    }
}