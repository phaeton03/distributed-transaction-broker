package com.example.correspodentbank.service;

import com.example.correspodentbank.domain.Transaction;
import com.example.correspodentbank.enums.Status;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    void saveTransaction(Transaction transaction);

    void updateTransaction(UUID trackingNumber, Status status);

    void deleteTransaction(UUID trackingNumber);

    List<Transaction> calculateBankCommitTransactions();

    List<Transaction> calculateSuccessBankTransactionsToFinancialInstitution();

    List<Transaction> showTransactions();
}