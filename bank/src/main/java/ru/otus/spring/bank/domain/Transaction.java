package ru.otus.spring.bank.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.otus.spring.bank.enums.Status;
import ru.otus.spring.bank.enums.TransactionType;

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
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(name = "account-graph",
        attributeNodes = {@NamedAttributeNode("account")})
@NamedEntityGraph(name = "client-account-graph",
        attributeNodes = {@NamedAttributeNode("client"), @NamedAttributeNode("account")})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TransactionType type;

    @Column(name = "create_dt")
    @CreatedDate
    private LocalDate createdDt;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "currency")
    private String currency;

    @Column(name = "tracking_number")
    private UUID trackingNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "is_distributed")
    private Boolean isDistributed;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "third_party_account_id")
    private Long thirdPartyAccountId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "client_id")
    private Client client;
}