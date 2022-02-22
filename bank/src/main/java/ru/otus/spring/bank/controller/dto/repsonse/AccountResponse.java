package ru.otus.spring.bank.controller.dto.repsonse;


import lombok.*;

import javax.persistence.Column;
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