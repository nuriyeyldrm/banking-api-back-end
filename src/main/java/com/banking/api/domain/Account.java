package com.banking.api.domain;

import io.dropwizard.validation.OneOf;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "balance must not be null")
    @Column(name = "balance", nullable = false)
    private Double balance;

    @NotNull(message = "account type must not be null")
    @OneOf({"CHECKING", "SAVING", "CREDIT_CARD", "INVESTING"})
    @Column(name = "account_type", nullable = false)
    private String accountType;

    @NotNull(message = "account status type must not be null")
    @OneOf({"ACTIVE", "SUSPENDED", "CLOSED"})
    @Column(name = "account_status_type", nullable = false)
    private String accountStatusType;

    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    public Account(){

    }

    public Account(Long id, Long userId, String description, Double balance, String accountType,
                   String accountStatusType, Timestamp createdDate) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.balance = balance;
        this.accountType = accountType;
        this.accountStatusType = accountStatusType;
        this.createdDate = createdDate;
    }

}
