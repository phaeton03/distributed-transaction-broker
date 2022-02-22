package com.example.correspodentbank.controller;

import com.example.correspodentbank.controller.dto.request.TransactionRequest;
import com.example.correspodentbank.enums.JournalType;
import com.example.correspodentbank.enums.Status;
import com.example.correspodentbank.logging.Audit;
import com.example.correspodentbank.mapper.TransactionMapper;
import com.example.correspodentbank.service.TransactionService;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank-correspodent")
public class BankCorrespodentTransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @Audit(value = "Регистрация транзакции в БК", type = JournalType.BANK_CORR)
    @PostMapping("/transaction")
    ResponseEntity<String> startTransaction(@RequestBody TransactionRequest transactionRequest) {

        transactionService.saveTransaction(transactionMapper.mapToBankTransaction(transactionRequest));
        return ResponseEntity.ok("");
    }

    @Audit(value = "Подтверждение транзакции в БК", type = JournalType.BANK_CORR)
    @PutMapping("/transaction")
    void confirmTransaction(@RequestBody TransactionRequest transactionRequest) {

        transactionService.updateTransaction(transactionRequest.getTrackingNumber(), Status.valueOf(transactionRequest.getStatus()));
    }

    @Audit(value = "Отмена транзакции в БК", type = JournalType.BANK_CORR)
    @DeleteMapping("/transaction")
    void cancelTransaction(@RequestBody TransactionRequest transactionRequest) {

        transactionService.deleteTransaction(transactionRequest.getTrackingNumber());
    }

    @Audit(value = "Показать все транзакции в БК", type = JournalType.BANK_CORR)
    @DeleteMapping("/transactions")
    List<TransactionRequest> showTransactions() {

        return transactionService.showTransactions().stream().map(transactionMapper::mapToBankTransactionRequest).collect(Collectors.toList());
    }
}