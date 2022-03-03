package ru.otus.spring.bank.controller.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.spring.bank.controller.dto.repsonse.AccountResponse;
import ru.otus.spring.bank.controller.dto.repsonse.ClientResponse;

@FeignClient(value = "financial-institution")
@RequestMapping("/financial-institution")
public interface FinancialInstitutionClient {

    @GetMapping("/{accountId}")
    ClientResponse getClient(@RequestParam("accountId") Long accountId);

    @GetMapping("/account/{accountId}")
    AccountResponse getAccount(@RequestParam("accountId") Long accountId);
}