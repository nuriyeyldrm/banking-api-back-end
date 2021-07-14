package com.banking.api.domain;

import io.dropwizard.validation.OneOf;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "transfers")
public class Transfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull(message = "receiver account id must not be null")
    private Long fromAccountId;

    @NotNull(message = "sender account id must not be null")
    private Long toAccountId;

    @NotNull
    private Long userId;

    @NotNull(message = "transaction amount must not be null")
    @DecimalMin("10")
    @Column(name = "transaction_amount", nullable = false)
    private Double transactionAmount;

    @NotNull
    @Column(name = "new_balance", nullable = false)
    private Double newBalance;

    @NotNull(message = "currency code must not be null")
    @OneOf({"TRY","USD","EUR"})
    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private Timestamp transactionDate;

    @NotNull(message = "description must not be null")
    @Column(name = "description", nullable = false)
    private String description;

    public Transfer(){

    }

    public Transfer(Long id, Long fromAccountId, Long toAccountId, Long userId, Double transactionAmount,
                    Double newBalance, String currencyCode, Timestamp transactionDate, String description) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.userId = userId;
        this.transactionAmount = transactionAmount;
        this.newBalance = newBalance;
        this.currencyCode = currencyCode;
        this.transactionDate = transactionDate;
        this.description = description;
    }

}
