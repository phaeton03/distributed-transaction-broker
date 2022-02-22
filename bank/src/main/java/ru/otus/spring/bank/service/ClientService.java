package ru.otus.spring.bank.service;

import ru.otus.spring.bank.controller.client.dto.request.TransactionRequest;
import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ClientService {
    List<Account> showAllAccounts(Long clientId);

    void openAccount(Long clientId, String currency, String type);

    void closeAccount(Long clientId, Long accountId);

    void deposit(Long clientId, Long accountId, BigDecimal amount);

    void withdrawal(Long clientId, Long accountId, BigDecimal amount);

    void transferMoneyToExternalClient(Long clientIdSender, Long accountIdSender, TransactionRequest transactionRequest);

    void transferMoneyToInternalClient(Long clientIdSender, Long accountIdSender, BigDecimal amount, Long receiverIdAccount);

    Client showClient(Long clientID);

    List<Client> showAllCLients();

}