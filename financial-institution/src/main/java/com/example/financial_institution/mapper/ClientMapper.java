package com.example.financial_institution.mapper;

import com.example.financial_institution.controller.dto.response.ClientResponse;
import com.example.financial_institution.domain.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ClientMapper {

    public ClientResponse mapToClient(Client client) {

         return ClientResponse.builder()
                .city(client.getCity())
                .name(client.getName())
                .build();
    }
}