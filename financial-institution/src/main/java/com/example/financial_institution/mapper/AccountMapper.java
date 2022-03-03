package com.example.financial_institution.mapper;

import com.example.financial_institution.controller.dto.response.AccountResponse;
import com.example.financial_institution.domain.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountResponse mapToAccountResponse(Account account) {

        return AccountResponse.builder()
                .currency(account.getCurrency())
                .id(account.getId())
                .amount(account.getAmount())
                .type(account.getType())
                .isBlock(account.getIsBlock())
                .build();
    }
}