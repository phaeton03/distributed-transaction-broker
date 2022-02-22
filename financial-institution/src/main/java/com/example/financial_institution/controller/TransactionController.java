package com.example.financial_institution.controller;

import com.example.financial_institution.controller.dto.request.TransactionRequest;
import com.example.financial_institution.enums.JournalType;
import com.example.financial_institution.logging.Audit;
import com.example.financial_institution.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/financial-institution/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @Audit(value = "Запрос информации о cуществовании транзакции", type = JournalType.CLIENT)
    @GetMapping("/{trackingNumber}")
    public Boolean existTransaction(@PathVariable(name = "trackingNumber") UUID trackingNumber) {

        return transactionService.existTransactionByTrackingNumber(trackingNumber);
    }

    @Audit(value = "Регистрация транзакции", type = JournalType.CLIENT)
    @PostMapping("/")
    public void startTransaction(@RequestBody List<TransactionRequest> transactionRequests) {
        transactionService.startTransaction(transactionRequests);
    }
}