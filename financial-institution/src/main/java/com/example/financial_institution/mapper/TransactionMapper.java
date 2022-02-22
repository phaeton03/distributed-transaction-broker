package com.example.financial_institution.mapper;

import com.example.financial_institution.controller.dto.request.TransactionRequest;
import com.example.financial_institution.domain.Transaction;
import com.example.financial_institution.enums.Status;
import com.example.financial_institution.enums.TransactionType;
import com.example.financial_institution.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionMapper {
    private final AccountService accountService;

    public Transaction mapToFinanciaInstitutionTransaction(TransactionRequest transactionRequest) {

        return Transaction.builder()
                .status(Status.valueOf(transactionRequest.getStatus()))
                .amount(transactionRequest.getAmount())
                .account(accountService.showAccount(transactionRequest.getAccountReceiverId()))
                .thirdPartyAccountId(transactionRequest.getAccountSenderId())
                .trackingNumber(transactionRequest.getTrackingNumber())
                .build();
    }
}