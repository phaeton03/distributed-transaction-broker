package ru.otus.spring.bank.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.bank.controller.dto.repsonse.TransactionResponse;
import ru.otus.spring.bank.domain.Transaction;

@Component
public class TransactionMapper {

    public TransactionResponse mapToTransactionResponse(Transaction transaction){

            return TransactionResponse.builder()
                    .amount(transaction.getAmount())
                    .type(transaction.getType())
                    .accountID(transaction.getAccount().getId())
                    .cdt(transaction.getCreatedDt())
                    .clientId(transaction.getClient().getId())
                    .currency(transaction.getCurrency())
                    .status(transaction.getStatus())
                    .build();
    }

}