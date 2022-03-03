package com.example.correspodentbank.mapper;

import com.example.correspodentbank.controller.dto.request.TransactionRequest;
import com.example.correspodentbank.domain.Transaction;
import com.example.correspodentbank.enums.Status;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TransactionMapper {

    public Transaction mapToBankTransaction(TransactionRequest transactionRequest) {

        return Transaction.builder()
                .bankStatus(Status.valueOf(transactionRequest.getStatus()))
                .amount(transactionRequest.getAmount())
                .accountReceiverId(transactionRequest.getAccountReceiverId())
                .accountSenderId(transactionRequest.getAccountSenderId())
                .trackingNumber(transactionRequest.getTrackingNumber())
                .ctd(LocalDate.now())
                .financialInstitutionStatus(Status.IN_PROGRESS)
                .build();
    }

    public TransactionRequest mapToBankTransactionRequest(Transaction transaction) {

        return TransactionRequest.builder()
                .status(transaction.getBankStatus().name())
                .accountSenderId(transaction.getAccountSenderId())
                .amount(transaction.getAmount())
                .accountReceiverId(transaction.getAccountReceiverId())
                .trackingNumber(transaction.getTrackingNumber())
                .build();
    }

    public TransactionRequest mapToFinancialInstituteTransactionRequest(Transaction transaction) {

        return TransactionRequest.builder()
                .status(Status.COMMIT.name())
                .accountSenderId(transaction.getAccountSenderId())
                .amount(transaction.getAmount())
                .accountReceiverId(transaction.getAccountReceiverId())
                .trackingNumber(transaction.getTrackingNumber())
                .build();
    }

}