package ru.otus.spring.bank.service;

import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.domain.Transaction;
import ru.otus.spring.bank.enums.Status;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    void openAccount(String type, String curency, Client client);

    void closeAccount(Account account);

    void deposit(Account account, BigDecimal amount);

    void withdrawal(Account account, BigDecimal amount);

    void closeAccount(Long accountId);

    List<Account> showAllAccounts();

    Account showClientAccount(Long accountId, Long clientId);

    Account showAccount(Long accountId);

    public Boolean checkAccounts(Long accountSenderId, Long accountReceiverId, BigDecimal amount);

    public BigDecimal getReserveMoney(Long accountId);

}