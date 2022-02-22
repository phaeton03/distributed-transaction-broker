package ru.otus.spring.bank.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.bank.controller.client.CorrespodentBankClient;
import ru.otus.spring.bank.controller.client.dto.request.TransactionRequest;
import ru.otus.spring.bank.enums.JournalType;
import ru.otus.spring.bank.logging.Audit;
import ru.otus.spring.bank.service.ClientService;
import ru.otus.spring.bank.service.TransactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
public class DistributedTransactionController {
    private final ClientService clientService;
    private final CorrespodentBankClient correspodentBankClient;
    private final TransactionService transactionService;

    @Audit(value = "Клиент сделал перевод на другой счет", type = JournalType.DISTRIBURED_TRANSACTION)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "accountId", value = "Account ID", required = true, dataType = "Long", example = "1"),
    })
    @PostMapping("/{clientId}/accounts/{accountId}/transfer/external")
    public void transferMoneyToExternalClient(@PathVariable("clientId") Long clientId,
                                              @PathVariable("accountId") Long accountId,
                                              @RequestBody TransactionRequest transactionRequest) {

        clientService.transferMoneyToExternalClient(clientId, accountId, transactionRequest);

    }

    @Audit(value = "Клиент сделал перевод на другой счет", type = JournalType.DISTRIBURED_TRANSACTION)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "accountId", value = "Account ID", required = true, dataType = "Long", example = "1"),
    })
    @PutMapping("/{clientId}/accounts/{accountId}/transfer/confirm")
    public void confirm(@PathVariable("clientId") Long clientId,
                        @PathVariable("accountId") Long accountId,
                        @RequestBody TransactionRequest transactionRequest) {

        correspodentBankClient.confirmTransaction(transactionRequest);
    }

    @Audit(value = "Клиент сделал перевод на другой счет", type = JournalType.DISTRIBURED_TRANSACTION)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "accountId", value = "Account ID", required = true, dataType = "Long", example = "1"),
    })
    @DeleteMapping("/{clientId}/accounts/{accountId}/transfer/cancel")
    public void cancel(@PathVariable("clientId") Long clientId,
                       @PathVariable("accountId") Long accountId,
                       @RequestBody TransactionRequest transactionRequest) {

        correspodentBankClient.cancelTransaction(transactionRequest);
        transactionService.cancelTransaction(transactionRequest.getTrackingNumber());
    }
}