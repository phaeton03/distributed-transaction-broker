package ru.otus.spring.bank.controller.dto.repsonse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyProfileResponse {
    private Long id;

    private List<AccountResponse> accounts;

    private String name;

    private String city;

    private List<TransactionResponse> transactions;
}