package ru.otus.spring.bank.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.spring.bank.controller.dto.repsonse.ClientResponse;
import ru.otus.spring.bank.controller.dto.repsonse.MyProfileResponse;
import ru.otus.spring.bank.domain.Client;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClientMapper {
    private final AccountMapper accountMapper;
    private final TransactionMapper transactionMapper;

    public MyProfileResponse mapToProfileResponse(Client client) {

        return MyProfileResponse.builder()
                .city(client.getCity())
                .id(client.getId())
                .name(client.getName())
                .accounts(client.getAccounts().stream().map(accountMapper::mapToAccountResponse).collect(Collectors.toList()))
                .transactions(client.getTransactions().stream().map(transactionMapper::mapToTransactionResponse).collect(Collectors.toList()))
                .build();
    }

    public ClientResponse mapToClientResponse(Client client) {

        return new ClientResponse(client.getName(), client.getCity());
    }
}