package ru.otus.spring.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.spring.bank.domain.Transaction;
import ru.otus.spring.bank.exception.NotFoundTransactionException;
import ru.otus.spring.bank.repository.TransactionRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImpTest {
    private final UUID TRACKING_NUMBER = UUID.randomUUID();

    @Mock
    private AccountService accountService;

    @Mock
    private ClientService clientService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AdminServiceImp adminService;

    @Test
    public void shouldTrowNotFoundTransactionException() {
        given(transactionRepository.findByTrackingNumber(TRACKING_NUMBER)).willReturn(Optional.empty());

        assertThatThrownBy(() -> adminService.showTransactionByTrackingNumber(TRACKING_NUMBER)).isInstanceOf(NotFoundTransactionException.class);
    }
}