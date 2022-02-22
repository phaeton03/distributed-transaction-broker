package com.example.financial_institution.service;

import com.example.financial_institution.domain.Account;
import com.example.financial_institution.exception.AccountNotExistException;
import com.example.financial_institution.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImpTest {

    private final Long ACCOUNT_ID = 1L;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImp accountService;

    @Test
    public void shouldThrowAccountNotExistException() {
        given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.showAccount(ACCOUNT_ID)).isInstanceOf(AccountNotExistException.class);
    }
}