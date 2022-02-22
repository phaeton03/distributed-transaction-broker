package com.example.financial_institution.service;

import com.example.financial_institution.domain.Account;
import com.example.financial_institution.exception.AccountNotExistException;
import com.example.financial_institution.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account showAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(AccountNotExistException::new);
    }
}