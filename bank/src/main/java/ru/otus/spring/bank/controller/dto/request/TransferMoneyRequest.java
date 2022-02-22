package ru.otus.spring.bank.controller.dto.request;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class TransferMoneyRequest {
    BigDecimal amount;
    Long accountId;
}