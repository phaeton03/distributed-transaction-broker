package ru.otus.spring.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.bank.controller.client.dto.request.TransactionRequest;
import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.domain.Transaction;
import ru.otus.spring.bank.exception.NotFoundTransactionException;
import ru.otus.spring.bank.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImp implements AdminService {
    private final AccountService accountService;
    private final ClientService clientService;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    @Override
    public List<Account> showAllAccounts() {
        return accountService.showAllAccounts();
    }

    @Override
    public List<Account> showAllAccountsByClient(Long clientId) {
        return clientService.showAllAccounts(clientId);
    }

    @Override
    public void closeAccount(Long accountId) {
        accountService.closeAccount(accountId);
    }

    @Override
    public Client showClient(Long clientId) {
        return clientService.showClient(clientId);
    }

    @Override
    public List<Client> showAllClients() {
        return clientService.showAllCLients();
    }

    @Override
    public List<Transaction> showAllTransactionsByClientForPeriod(LocalDate start, LocalDate end, Long clientId) {
        return transactionService.showTransactionsForPeriod(start, end, clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction showTransactionByTrackingNumber(UUID trackingNumber) {
        return transactionRepository.findByTrackingNumber(trackingNumber).orElseThrow(NotFoundTransactionException::new);
    }
}