package com.example.financial_institution.controller.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class TransactionRequest {
    private BigDecimal amount;

    private String status;

    private String type;

    private Long accountSenderId;

    private Long accountReceiverId;

    private UUID trackingNumber;
}