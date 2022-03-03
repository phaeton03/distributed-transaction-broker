package ru.otus.spring.bank.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.bank.controller.client.FinancialInstitutionClient;
import ru.otus.spring.bank.controller.dto.repsonse.AccountResponse;
import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.domain.Transaction;
import ru.otus.spring.bank.enums.Status;
import ru.otus.spring.bank.exception.*;
import ru.otus.spring.bank.repository.AccountRepository;
import ru.otus.spring.bank.repository.ClientRepository;
import ru.otus.spring.bank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImp implements AccountService {
    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final AccountServiceImp self;
    private final FinancialInstitutionClient financialInstitutionClient;

    public AccountServiceImp(AccountRepository accountRepository, ClientRepository clientRepository,
                             TransactionRepository transactionRepository, @Lazy AccountServiceImp self,
                             FinancialInstitutionClient financialInstitutionClient) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
        this.self = self;
        this.financialInstitutionClient = financialInstitutionClient;
    }

    @Override
    public void openAccount(String type, String currency, Client client) {

        Account account = Account.builder()
                .currency(currency)
                .client(client)
                .amount(BigDecimal.ZERO)
                .type(type)
                .build();

        accountRepository.save(account);
    }

    @Override
    public void closeAccount(Account account) {

        accountRepository.delete(account);
    }

    @Override
    public void closeAccount(Long accountId) {

        accountRepository.deleteById(accountId);
    }

    @Override
    public void deposit(Account account, BigDecimal amount) {
        if (account.getIsBlock() == true) {
            throw new AccountBlockException();
        }

        final BigDecimal add = account.getAmount().add(amount);
        account.setAmount(add);
    }

    @Override
    public void withdrawal(Account account, BigDecimal amount) {

        if (account.getIsBlock() == true) {
            throw new AccountBlockException();
        }

        if (amount.compareTo(BigDecimal.ZERO) == -1) {
            throw new NegativeAmountException();
        }

        if (account.getAmount().subtract(self.getReserveMoney(account.getId())).compareTo(amount) == -1) {

            throw new InsufficientMoneyException();
        }

        BigDecimal total = account.getAmount().subtract(amount);
        account.setAmount(total);
        accountRepository.save(account);
    }

    @Override
    public List<Account> showAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account showAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(AccountNotExistException::new);
    }

    @Override
    public Account showClientAccount(Long accountId, Long clientId) {
        final Client client = clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);

        return client.getAccounts().stream()
                .filter(a -> a.getId().equals(accountId))
                .findFirst().orElseThrow(AccountNotExistException::new);
    }

    @Override
    public Boolean checkAccounts(Long accountSenderId, Long accountReceiverId, BigDecimal amount) {
        final Account accountSender = showAccount(accountSenderId);
        final AccountResponse accountReceiver = financialInstitutionClient.getAccount(accountReceiverId);

        if (accountSender.getIsBlock() || accountReceiver.getIsBlock()) {
            throw new AccountBlockException();
        }
        if (accountSender.getAmount().subtract(self.getReserveMoney(accountSenderId)).compareTo(amount) == -1) {
            throw new InsufficientMoneyException();
        }
        if (amount.compareTo(BigDecimal.ZERO) == -1) {
            throw new NegativeAmountException();
        }

        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getReserveMoney(Long accountId) {

        return transactionRepository.findByAccountIdAndStatusInAndIsDistributedIsTrue(accountId, List.of(Status.IN_PROGRESS, Status.COMMIT))
                .stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }
}