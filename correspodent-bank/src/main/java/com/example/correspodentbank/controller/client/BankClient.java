package com.example.correspodentbank.controller.client;

import com.example.correspodentbank.controller.dto.request.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(value = "bank")
@RequestMapping("/bank")
public interface BankClient {

    @PutMapping("/transaction")
    void confirmTransactionInBank(List<TransactionRequest> transactionRequest);

}