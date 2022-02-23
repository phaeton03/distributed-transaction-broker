package com.example.correspodentbank.service;

import com.example.correspodentbank.controller.client.BankClient;
import com.example.correspodentbank.controller.client.FinancialInstitutionClient;
import com.example.correspodentbank.controller.dto.request.TransactionRequest;
import com.example.correspodentbank.domain.Transaction;
import com.example.correspodentbank.enums.Status;
import com.example.correspodentbank.mapper.TransactionMapper;
import com.example.correspodentbank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankClient bankClient;
    private final FinancialInstitutionClient financialInstitutionClient;
    private final TransactionMapper transactionMapper;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void updateTransaction(UUID trackingNumber, Status status) {
        transactionRepository.updateTransactionByBankStatusAndTrackingNumber(status, trackingNumber);
    }

    @Transactional
    public void deleteTransaction(UUID trackingNumber) {
        transactionRepository.deleteByTrackingNumberAndBankStatus(trackingNumber, Status.IN_PROGRESS);
    }

    @Override
    @Transactional()
    public List<Transaction> calculateBankCommitTransactions() {
        List<Transaction> commitTransactions = transactionRepository.getAllByBankStatus(Status.COMMIT);

        if (!commitTransactions.isEmpty()) {
            final List<TransactionRequest> transactionRequests =
                    commitTransactions.stream().map(transactionMapper::mapToBankTransactionRequest).collect(Collectors.toList());

            bankClient.confirmTransactionInBank(transactionRequests);
            commitTransactions.forEach(transaction -> transaction.setBankStatus(Status.SUCCESS));

            commitTransactions = transactionRepository.saveAll(commitTransactions);
        }

        return commitTransactions;
    }

    @Transactional()
    public List<Transaction> calculateSuccessBankTransactionsToFinancialInstitution() {
        List<Transaction> successTransactions =
                transactionRepository.getAllByBankStatusAndFinancialInstitutionStatusIsNot(Status.SUCCESS, Status.SUCCESS);

        final List<TransactionRequest> transactionRequests = successTransactions.stream()
                .filter(t -> !financialInstitutionClient.existTransaction(t.getTrackingNumber()))
                .map(transactionMapper::mapToFinancialInstituteTransactionRequest).collect(Collectors.toList());

        if (!transactionRequests.isEmpty()) {

            financialInstitutionClient.startTransaction(transactionRequests);
            successTransactions.forEach(transaction -> transaction.setFinancialInstitutionStatus(Status.SUCCESS));

            successTransactions = transactionRepository.saveAll(successTransactions);
        }

        return successTransactions;
    }

    @Override
    public List<Transaction> showTransactions() {

        return transactionRepository.findAll();
    }
}