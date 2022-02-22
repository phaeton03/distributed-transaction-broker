package ru.otus.spring.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.bank.controller.client.CorrespodentBankClient;
import ru.otus.spring.bank.controller.client.dto.request.TransactionRequest;
import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.domain.Transaction;
import ru.otus.spring.bank.enums.TransactionType;
import ru.otus.spring.bank.exception.ClientNotFoundException;
import ru.otus.spring.bank.repository.ClientRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImp implements ClientService {
    private final ClientRepository clientRepository;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final CorrespodentBankClient correspodentBankClient;

    @Override
    public List<Account> showAllAccounts(Long clientId) {

        return clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new).getAccounts();
    }

    @Override
    public void openAccount(Long clientId, String currency, String type) {
        final Client client = clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
        accountService.openAccount(type, currency, client);
    }

    @Override
    public void closeAccount(Long clientId, Long accountId) {

        accountService.closeAccount(accountService.showClientAccount(accountId, clientId));
    }

    @Override
    @Transactional
    public void deposit(Long clientId, Long accountId, BigDecimal amount) {

        Account account = accountService.showClientAccount(accountId, clientId);
        accountService.deposit(account, amount);
        transactionService.recordLocalTransaction(TransactionType.DEPOSIT, amount, accountId, account.getClient());
    }

    @Override
    @Transactional
    public void withdrawal(Long clientId, Long accountId, BigDecimal amount) {

        Account account = accountService.showClientAccount(accountId, clientId);
        accountService.withdrawal(account, amount);
        transactionService.recordLocalTransaction(TransactionType.WITHDRAWAL, amount, accountId, account.getClient());
    }

    @Override
    public void transferMoneyToExternalClient(Long clientIdSender, Long accountIdSender, TransactionRequest transactionRequest) {

        UUID trackingNumber = UUID.randomUUID();

        transactionRequest.setAccountSenderId(accountIdSender);
        transactionRequest.setTrackingNumber(trackingNumber);
        accountService.checkAccounts(accountIdSender, transactionRequest.getAccountReceiverId(), transactionRequest.getAmount());
        correspodentBankClient.startTransaction(transactionRequest);

        transactionService.recordGlobalTransaction(TransactionType.DEBIT, transactionRequest.getAmount(),
                accountIdSender, showClient(clientIdSender), transactionRequest.getAccountReceiverId(), trackingNumber);
    }

    @Override
    @Transactional
    public void transferMoneyToInternalClient(Long clientIdSender, Long accountIdSender, BigDecimal amount, Long receiverIdAccount) {
        final Account accountSender = accountService.showClientAccount(accountIdSender, clientIdSender);
        final Account accountReceiver = accountService.showAccount(receiverIdAccount);

        accountService.withdrawal(accountSender, amount);
        accountService.deposit(accountReceiver, amount);

        transactionService.recordLocalTransaction(amount, accountIdSender, accountSender.getClient(), receiverIdAccount);
    }

    @Override
    public Client showClient(Long clientID) {
        return clientRepository.findById(clientID).orElseThrow(ClientNotFoundException::new);
    }

    @Override
    public List<Client> showAllCLients() {
        return clientRepository.findAll();
    }

}