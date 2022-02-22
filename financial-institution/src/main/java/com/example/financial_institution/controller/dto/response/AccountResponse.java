package com.example.financial_institution.controller.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Long id;

    private String type;

    private BigDecimal amount;

    private String currency;

    private Boolean isBlock;
}