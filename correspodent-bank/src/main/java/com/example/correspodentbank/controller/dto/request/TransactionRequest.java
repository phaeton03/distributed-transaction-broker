package com.example.correspodentbank.controller.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class TransactionRequest {
    private BigDecimal amount;
    private String status;
    private Long accountSenderId;
    private Long accountReceiverId;
    private UUID trackingNumber;
}