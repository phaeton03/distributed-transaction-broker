package com.example.financial_institution.controller;

import com.example.financial_institution.controller.dto.response.AccountResponse;
import com.example.financial_institution.controller.dto.response.ClientResponse;
import com.example.financial_institution.enums.JournalType;
import com.example.financial_institution.logging.Audit;
import com.example.financial_institution.mapper.AccountMapper;
import com.example.financial_institution.mapper.ClientMapper;
import com.example.financial_institution.service.AccountService;
import com.example.financial_institution.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/financial-institution")
public class ClientController {
    private final ClientService clientService;
    private final AccountService accountService;
    private final ClientMapper clientMapper;
    private final AccountMapper accountMapper;

    @Audit(value = "Запрос информации о клиенте в финансовом учреждении", type = JournalType.CLIENT)
    @GetMapping("/{accountId}")
    public ClientResponse showClient(@PathVariable Long accountId) {

        return clientMapper.mapToClient(clientService.showClient(accountId));
    }

    @Audit(value = "Запрос информации о счете в финансовом учреждении", type = JournalType.CLIENT)
    @GetMapping("/account/{accountId}")
    public AccountResponse showAccount(@PathVariable Long accountId) {

        return accountMapper.mapToAccountResponse(accountService.showAccount(accountId));
    }
}