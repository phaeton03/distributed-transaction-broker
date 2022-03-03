package ru.otus.spring.bank.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.otus.spring.bank.controller.dto.repsonse.AccountResponse;
import ru.otus.spring.bank.controller.dto.repsonse.MyProfileResponse;
import ru.otus.spring.bank.controller.dto.repsonse.TransactionResponse;
import ru.otus.spring.bank.controller.dto.request.PeriodRequest;
import ru.otus.spring.bank.enums.JournalType;
import ru.otus.spring.bank.logging.Audit;
import ru.otus.spring.bank.mapper.AccountMapper;
import ru.otus.spring.bank.mapper.ClientMapper;
import ru.otus.spring.bank.mapper.TransactionMapper;
import ru.otus.spring.bank.service.AdminService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank/admin")
public class AdminController {
    private final AdminService adminService;
    private final AccountMapper accountMapper;
    private final ClientMapper clientMapper;
    private final TransactionMapper transactionMapper;

    @Audit(value = "Менеджер запросил данные по всем аккаунтам", type = JournalType.ADMIN)
    @GetMapping("/accounts")
    public List<AccountResponse> showAllAccounts() {

        return adminService.showAllAccounts()
                .stream().map(accountMapper::mapToAccountResponse).collect(Collectors.toList());
    }

    @Audit(value = "Менеджер запросил данные по аккаунту клиента", type = JournalType.ADMIN)
    @GetMapping("/{clientId}/accounts")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
    })
    public List<AccountResponse> showAccountByClient(@PathVariable Long clientId) {

        return adminService.showAllAccountsByClient(clientId)
                .stream().map(accountMapper::mapToAccountResponse).collect(Collectors.toList());
    }

    @Audit(value = "Менеджер закрыл аккаунт клиента", type = JournalType.ADMIN)
    @DeleteMapping("/accounts/{accountId}/close")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "AccountId", required = true, dataType = "Long", example = "1"),
    })
    public void closeAccount(@PathVariable("accountId") Long accountId) {
        adminService.closeAccount(accountId);
    }

    @Audit(value = "Менеджер запросил информацию по клиенту", type = JournalType.ADMIN)
    @GetMapping("/{clientId}")
    public MyProfileResponse showClient(@PathVariable("clientId") Long clientId) {

        return clientMapper.mapToProfileResponse(adminService.showClient(clientId));
    }

    @Audit(value = "Менеджер запросил информацию по всем клиентам", type = JournalType.ADMIN)
    @GetMapping("/clients")
    public List<MyProfileResponse> showAllClients() {

        return adminService.showAllClients().stream()
                .map(clientMapper::mapToProfileResponse).collect(Collectors.toList());
    }

    @Audit(value = "Менеджер запросил информацию по всем транзакциям клиента за период", type = JournalType.ADMIN)
    @GetMapping("/transactions")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "Client ID", required = true, dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "start", value = "2022-02-15", required = true, dataType = "LocalDate", example = "2022-02-15"),
            @ApiImplicitParam(name = "end", value = "2022-02-18", required = true, dataType = "LocalDate", example = "2022-02-18"),
    })
    public List<TransactionResponse> showAllTransactions(@RequestParam Long clientId,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        return adminService.showAllTransactionsByClientForPeriod(start, end, clientId)
                .stream().map(transactionMapper::mapToTransactionResponse).collect(Collectors.toList());
    }

    @Audit(value = "Менеджер запросил информацию по транзакции с конкретным tracking number", type = JournalType.ADMIN)
    @GetMapping("/transaction")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "trackingNumber", value = "TrackingNumber", required = true, dataType = "String", example = "123e4567-e89b-12d3-a456-426614174000"),
    })
    public TransactionResponse showTransactionByTrackingNumber(@RequestParam UUID trackingNumber) {
        return transactionMapper.mapToTransactionResponse(adminService.showTransactionByTrackingNumber(trackingNumber));
    }
}