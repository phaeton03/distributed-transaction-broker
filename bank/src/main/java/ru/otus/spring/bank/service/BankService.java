package ru.otus.spring.bank.service;

import ru.otus.spring.bank.domain.Client;

import java.math.BigDecimal;

public interface BankService {

    Client showClient(Long accountId);
}