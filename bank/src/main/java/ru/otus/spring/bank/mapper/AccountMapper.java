package ru.otus.spring.bank.mapper;

import org.springframework.stereotype.Component;
import ru.otus.spring.bank.controller.dto.repsonse.AccountResponse;
import ru.otus.spring.bank.domain.Account;

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