package com.example.financial_institution.service;

import com.example.financial_institution.domain.Client;
import com.example.financial_institution.exception.AccountNotExistException;
import com.example.financial_institution.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientServiceImp implements ClientService {
    private final AccountRepository accountRepository;

    @Override
    public Client showClient(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(AccountNotExistException::new).getClient();
    }
}