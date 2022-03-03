package com.example.financial_institution.service;

import com.example.financial_institution.controller.dto.request.TransactionRequest;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    Boolean existTransactionByTrackingNumber(UUID trackingNumber);

    void startTransaction(List<TransactionRequest> transactionRequests);

    void calculateCommitTransactions();
}