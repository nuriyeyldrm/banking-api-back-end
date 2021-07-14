package com.banking.api.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Integer transactionId;

    @NotNull
    private Integer categoryId;

    @NotNull
    private Integer userId;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Double amount;

    @NotNull
    @Column(name = "note", nullable = false)
    private String note;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private Long transactionDate;

    public Transaction(){

    }

    public Transaction(Integer transactionId, Integer categoryId, Integer userId,
                       Double amount, String note, Long transactionDate) {
        this.transactionId = transactionId;
        this.categoryId = categoryId;
        this.userId = userId;
        this.amount = amount;
        this.note = note;
        this.transactionDate = transactionDate;
    }
}
