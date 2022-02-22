package ru.otus.spring.bank.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.bank.controller.client.CorrespodentBankClient;
import ru.otus.spring.bank.controller.client.FinancialInstitutionClient;
import ru.otus.spring.bank.controller.client.dto.request.TransactionRequest;
import ru.otus.spring.bank.controller.dto.repsonse.ClientResponse;
import ru.otus.spring.bank.enums.JournalType;
import ru.otus.spring.bank.enums.Status;
import ru.otus.spring.bank.logging.Audit;
import ru.otus.spring.bank.mapper.ClientMapper;
import ru.otus.spring.bank.service.BankService;
import ru.otus.spring.bank.service.TransactionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
public class BankController {
    private final BankService bankService;
    private final ClientMapper clientMapper;
    private final TransactionService transactionService;
    private final FinancialInstitutionClient financialInstitutionClient;
    private final CorrespodentBankClient correspodentBankClient;

    @Audit(value = "Банк отправил информацию по клиенту по переводу внутри банка", type = JournalType.BANK)
    @GetMapping("/transfer/local/{accountId}")
    public ClientResponse showInternalClient(@PathVariable Long accountId) {

        return clientMapper.mapToClientResponse(bankService.showClient(accountId));
    }

    @Audit(value = "Банк отправил информацию по клиенту по переводу из другого банка", type = JournalType.BANK)
    @GetMapping("/bank-correspodent/transfer/global/{accountId}")
    public ClientResponse showExternalClient(@PathVariable Long accountId) {

        return financialInstitutionClient.getClient(accountId);
    }

    @Audit(value = "Банк отправляет в банк-корресподент распоряжение о переводе средств", type = JournalType.BANK)
    @PostMapping("/bank-correspodent/transaction")
    public void startTransactionInBankCorrespodent(@RequestBody TransactionRequest transactionRequest) {

         correspodentBankClient.startTransaction(transactionRequest);
    }

    @Audit(value = "", type = JournalType.BANK)
    @PutMapping("/bank-correspodent/transaction")
    public void commitTransactionInBankCorrespodent(@RequestBody TransactionRequest transactionRequest) {

        transactionRequest.setStatus(Status.SUCCESS.name());
        correspodentBankClient.confirmTransaction(transactionRequest);
    }

    @Audit(value = "Из банк-корресподента пришло подтверждение транзакции", type = JournalType.BANK)
    @PutMapping("/transaction")
    public void commitTransactionInBank(@RequestBody List<TransactionRequest> transactionRequest) {

        transactionService.findAllTransactionByTrackingNumber(transactionRequest);
    }
}