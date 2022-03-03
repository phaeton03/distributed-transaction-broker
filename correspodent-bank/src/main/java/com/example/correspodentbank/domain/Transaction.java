package com.example.correspodentbank.domain;

import com.example.correspodentbank.enums.Status;
import com.example.correspodentbank.enums.TransactionType;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "transactions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "created_dt")
    private LocalDate ctd;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_status")
    private Status bankStatus;

    @Column(name = "currency")
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "financial_institution_status")
    private Status financialInstitutionStatus = Status.IN_PROGRESS;

    @Column(name = "account_sender_id")
    private Long accountSenderId;

    @Column(name = "account_receiver_id")
    private Long accountReceiverId;

    @Column(name = "tracking_number")
    private UUID trackingNumber;
}