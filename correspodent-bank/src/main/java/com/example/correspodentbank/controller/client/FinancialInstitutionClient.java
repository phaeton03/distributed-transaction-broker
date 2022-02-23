package com.example.correspodentbank.controller.client;

import com.example.correspodentbank.controller.dto.request.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(value = "financial-institution")
@RequestMapping("/financial-institution/transaction")
public interface FinancialInstitutionClient {

    @PostMapping("/")
    void startTransaction(List<TransactionRequest> transactionRequest);

    @GetMapping("/{trackingNumber}")
    Boolean existTransaction(@PathVariable(name = "trackingNumber") UUID trackingNumber);
}