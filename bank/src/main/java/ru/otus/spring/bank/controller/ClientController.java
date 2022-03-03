package ru.otus.spring.bank.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.bank.controller.client.CorrespodentBankClient;
import ru.otus.spring.bank.controller.client.dto.request.TransactionRequest;
import ru.otus.spring.bank.controller.dto.repsonse.AccountResponse;
import ru.otus.spring.bank.controller.dto.repsonse.MyProfileResponse;
import ru.otus.spring.bank.controller.dto.repsonse.TransactionResponse;
import ru.otus.spring.bank.controller.dto.request.PeriodRequest;
import ru.otus.spring.bank.controller.dto.request.TransferMoneyRequest;
import ru.otus.spring.bank.enums.JournalType;
import ru.otus.spring.bank.enums.TransactionType;
import ru.otus.spring.bank.logging.Audit;
import ru.otus.spring.bank.mapper.AccountMapper;
import ru.otus.spring.bank.mapper.ClientMapper;
import ru.otus.spring.bank.mapper.TransactionMapper;
import ru.otus.spring.bank.service.AccountService;
import ru.otus.spring.bank.service.ClientService;
import ru.otus.spring.bank.service.TransactionService;

import javax.websocket.server.PathParam;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank/client")
public class ClientController {
    private final ClientService clientService;
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;
    private final AccountMapper accountMapper;
    private final ClientMapper clientMapper;

    @Audit(value = "Клиент открыл счет", type = JournalType.CLIENT)
    @PostMapping("/{clientId}/accounts/open")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "currency", value = "Currency", required = true, dataType = "String", example = "RUB"),
            @ApiImplicitParam(name = "type", value = "Type", required = true, dataType = "String", example = "DEBIT"),
    })
    public void openAccount(@PathVariable("clientId") Long clientId,
                            @RequestParam String currency,
                            @RequestParam String type) {

        clientService.openAccount(clientId, currency, type);
    }

    @Audit(value = "Клиент закрыл счет", type = JournalType.CLIENT)
    @DeleteMapping("/{clientId}/accounts/{accountId}/close")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "accountId", value = "Account ID", required = true, dataType = "Long", example = "1"),
    })
    public void closeAccount(@PathVariable("clientId") Long clientId, @PathVariable("accountId") Long accountId) {

        clientService.closeAccount(clientId, accountId);
    }

    @Audit(value = "Клиент пополнил счет", type = JournalType.CLIENT)
    @PatchMapping("/{clientId}/accounts/{accountId}/deposit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "accountId", value = "Account ID", required = true, dataType = "Long", example = "1"),
    })
    public void deposit(@PathVariable("clientId") Long clietnId,
                        @PathVariable("accountId") Long accountId,
                        @RequestBody BigDecimal amount) {

        clientService.deposit(clietnId, accountId, amount);
    }

    @Audit(value = "Клиент снял деньги со счета", type = JournalType.CLIENT)
    @PatchMapping("/{clientId}/accounts/{accountId}/withdrawal")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "accountId", value = "Account ID", required = true, dataType = "Long", example = "1"),
    })
    public void withdrawal(@PathVariable("clientId") Long clientId,
                           @PathVariable("accountId") Long accountId,
                           @RequestBody BigDecimal amount) {

        clientService.withdrawal(clientId, accountId, amount);
    }

    @Audit(value = "Клиент запросил информацию по всем своим счетам", type = JournalType.CLIENT)
    @GetMapping("/{clientId}/accounts")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
    })
    public List<AccountResponse> getAccounts(@PathVariable("clientId") Long clietnId) {

        return clientService.showAllAccounts(clietnId).stream()
                .map(accountMapper::mapToAccountResponse).collect(Collectors.toList());
    }

    @Audit(value = "Клиент сделал перевод на другой счет", type = JournalType.CLIENT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "accountId", value = "Account ID", required = true, dataType = "Long", example = "1"),
    })
    @PostMapping("/{clientId}/accounts/{accountId}/transfer")
    public void transferMoneyToInternalClient(@PathVariable("clientId") Long clientId,
                                              @PathVariable("accountId") Long accountId,
                                              @RequestBody TransferMoneyRequest transferMoneyRequest) {

        clientService.transferMoneyToInternalClient(clientId, accountId, transferMoneyRequest.getAmount(), transferMoneyRequest.getAccountId());
    }

    @Audit(value = "Клиент запросил информацию о своем профиле", type = JournalType.CLIENT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
    })
    @GetMapping("/{clientId}")
    public MyProfileResponse getClientInfo(@PathVariable("clientId") Long clientId) {

        return clientMapper.mapToProfileResponse(clientService.showClient(clientId));
    }

    @Audit(value = "Клиент запросил информацию о последней транзакции", type = JournalType.CLIENT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
    })
    @GetMapping("/transaction/{clientId}")
    public TransactionResponse getLastTransaction(@PathVariable("clientId") Long clientId) {

        return transactionMapper.mapToTransactionResponse(transactionService.showLastTransaction(clientId));
    }

    @Audit(value = "Клиент запросил информацию о транзакциях за период", type = JournalType.CLIENT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
    })
    @GetMapping("/transactions/{clientId}")
    public List<TransactionResponse> getTransactionByPeriod(@PathVariable("clientId") Long clientId, PeriodRequest period) {

        return transactionService.showTransactionsForPeriod(period.getStart(), period.getEnd(), clientId)
                        .stream().map(transactionMapper::mapToTransactionResponse).collect(Collectors.toList());
    }

    @Audit(value = "Клиент запросил информацию о десяти последних транзакциях", type = JournalType.CLIENT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
    })
    @GetMapping("/transactions/10/{clientId}")
    public List<TransactionResponse> getLastTenTransactions(@PathVariable("clientId") Long clientId) {

        return transactionService.showLast10Transaction(clientId)
                .stream().map(transactionMapper::mapToTransactionResponse).collect(Collectors.toList());
    }


}