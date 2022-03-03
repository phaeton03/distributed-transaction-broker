package ru.otus.spring.bank.controller.dto.repsonse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import ru.otus.spring.bank.domain.Client;
import ru.otus.spring.bank.enums.Status;
import ru.otus.spring.bank.enums.TransactionType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private TransactionType type;

    private LocalDate cdt;

    private BigDecimal amount;

    private String currency;

    private Status status;

    private String sender;

    private Long accountID;

    private Long clientId;

}