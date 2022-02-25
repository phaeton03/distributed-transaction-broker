package ru.otus.spring.bank.controller.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.bank.controller.client.dto.request.TransactionRequest;
import ru.otus.spring.bank.controller.dto.repsonse.ClientResponse;

@FeignClient(value = "bank-correspodent")
@RequestMapping("/bank-correspodent")
public interface CorrespodentBankClient {
    @PostMapping("/transaction")
    ResponseEntity<String> startTransaction(TransactionRequest transactionRequest);

    @PutMapping("/transaction")
    void confirmTransaction(TransactionRequest transactionRequest);

    @DeleteMapping("/transaction")
    void cancelTransaction(TransactionRequest transactionRequest);

}