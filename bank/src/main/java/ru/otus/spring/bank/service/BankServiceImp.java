package ru.otus.spring.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.exception.AccountBlockException;
import ru.otus.spring.bank.exception.InsufficientMoneyException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BankServiceImp implements BankService {
    private final AccountService accountService;

    @Override
    @Transactional(readOnly = true)
    public Client showClient(Long accountId) {
        return accountService.showAccount(accountId).getClient();
    }

}