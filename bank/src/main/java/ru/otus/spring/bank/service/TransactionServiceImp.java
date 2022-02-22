package ru.otus.spring.bank.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.spring.bank.controller.client.CorrespodentBankClient;
import ru.otus.spring.bank.controller.client.dto.request.TransactionRequest;
import ru.otus.spring.bank.domain.Account;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.domain.Transaction;
import ru.otus.spring.bank.enums.Status;
import ru.otus.spring.bank.enums.TransactionType;
import ru.otus.spring.bank.exception.AccountNotExistException;
import ru.otus.spring.bank.exception.IncorrectValueAmountException;
import ru.otus.spring.bank.exception.NotFoundTransactionException;
import ru.otus.spring.bank.repository.AccountRepository;
import ru.otus.spring.bank.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CorrespodentBankClient correspodentBankClient;

    @Override
    public Transaction recordLocalTransaction(TransactionType type, BigDecimal amount, Long accountId, Client client) {
        final Account account = accountRepository.findById(accountId).orElseThrow(AccountNotExistException::new);

        Transaction transaction = Transaction.builder()
                .status(Status.IN_PROGRESS)
                .type(type)
                .amount(amount)
                .account(account)
                .client(client)
                .isDistributed(false)
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void recordLocalTransaction(BigDecimal amount, Long accountId, Client client, Long accountIdReceiver) {
        Transaction transactionSender = recordLocalTransaction(TransactionType.DEBIT, amount, accountId, client);
        transactionSender.setThirdPartyAccountId(accountIdReceiver);

        Transaction transactionReceiver = recordLocalTransaction(TransactionType.CREDIT, amount, accountId, client);
        transactionSender.setThirdPartyAccountId(accountId);

        transactionRepository.save(transactionSender);
        transactionRepository.save(transactionReceiver);
    }

    @Override
    public Transaction recordGlobalTransaction(TransactionType type, BigDecimal amount,
                                               Long accountId, Client client, Long accountIdReceiver, UUID trackingNumber) {

        final Account account = accountRepository.findById(accountId).orElseThrow(AccountNotExistException::new);

        Transaction transaction = Transaction.builder()
                .status(Status.IN_PROGRESS)
                .type(type)
                .amount(amount)
                .account(account)
                .client(client)
                .isDistributed(true)
                .trackingNumber(trackingNumber)
                .thirdPartyAccountId(accountIdReceiver)
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Transaction showLastTransaction(Long clientId) {
        return transactionRepository.findFirstByClientIdOrderByCreatedDtDesc(clientId).orElseThrow(NotFoundTransactionException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> showTransactionsForPeriod(LocalDate start, LocalDate end, Long clientId) {
        return transactionRepository.findByCreatedDtBetweenAndClientId(start, end, clientId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Transaction> showLast10Transaction(Long clientId) {
        return transactionRepository.findFirst10ByClientIdOrderByCreatedDtDesc(clientId);
    }

    @Override
    @Transactional()
    public void findTransactionByTrackingNumber(TransactionRequest transactionRequest) {

        final Optional<Transaction> transaction = transactionRepository
                .findByTrackingNumberAndStatus(transactionRequest.getTrackingNumber(), Status.IN_PROGRESS);

        if (transaction.isPresent()) {
            transaction.get().setStatus(Status.valueOf(transactionRequest.getStatus()));
            transactionRepository.save(transaction.get());
            transactionRequest.setStatus(Status.SUCCESS.name());
            correspodentBankClient.confirmTransaction(transactionRequest);
        }
        if (!transactionRepository.existsByTrackingNumber(transactionRequest.getTrackingNumber())) {
            transactionRequest.setStatus(Status.SUCCESS.name());
            correspodentBankClient.confirmTransaction(transactionRequest);
        } else {
            correspodentBankClient.cancelTransaction(transactionRequest);
        }
    }

    @Transactional()
    @Override
    public void findAllTransactionByTrackingNumber(List<TransactionRequest> transactionRequestList) {

        transactionRequestList.forEach(this::findTransactionByTrackingNumber);
    }

    @Override
    @Transactional()
    public List<Transaction> calculateCommitTransactions() {
        final List<Transaction> commitTransactions = transactionRepository.findByStatus(Status.COMMIT);
        if (!commitTransactions.isEmpty()) {
            for (Transaction tx : commitTransactions) {
                final BigDecimal subtract = tx.getAccount().getAmount().subtract(tx.getAmount());

                if (subtract.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IncorrectValueAmountException();
                }

                tx.getAccount().setAmount(subtract);
            }
        }

        commitTransactions.forEach(transaction -> transaction.setStatus(Status.SUCCESS));

        return transactionRepository.saveAll(commitTransactions);
    }

    @Override
    @Transactional
    public void cancelTransaction(UUID trackingNumber) {
        transactionRepository.deleteByTrackingNumber(trackingNumber);
    }
}