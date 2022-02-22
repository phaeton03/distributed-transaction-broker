package ru.otus.spring.bank.service;

import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.domain.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AdminService {
    List<Account> showAllAccounts();

    List<Account> showAllAccountsByClient(Long clientId);

    void closeAccount(Long accountId);

    Client showClient(Long clientId);

    List<Client> showAllClients();

    Transaction showTransactionByTrackingNumber(UUID trackingNumber);

    List<Transaction> showAllTransactionsByClientForPeriod(LocalDate start, LocalDate end, Long clientId);
}