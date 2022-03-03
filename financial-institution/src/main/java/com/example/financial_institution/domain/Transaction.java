package com.example.financial_institution.domain;

import com.example.financial_institution.enums.Status;
import com.example.financial_institution.enums.TransactionType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedEntityGraph(name = "account-graph", attributeNodes = {@NamedAttributeNode("account")})
public class Transaction {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "created_dt")
    @CreatedDate
    private LocalDate cdt;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type = TransactionType.CREDIT;

    @Column(name = "third_party_account_id")
    private Long thirdPartyAccountId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "tracking_number")
    private UUID trackingNumber;
}