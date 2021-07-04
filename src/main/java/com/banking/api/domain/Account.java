package com.banking.api.domain;

import com.banking.api.domain.enumeration.AccountStatusType;
import com.banking.api.domain.enumeration.AccountType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

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

    @NotNull
    @Column(name = "balance", nullable = false)
    private Integer balance;

    @NotNull
    @Column(name = "account_type", nullable = false)
    private String accountType;

    @NotNull
    @Column(name = "account_status_type", nullable = false)
    private String accountStatusType;

    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    public Account(){

    }

    public Account(Long id, Long userId, String description, Integer balance, String accountType,
                   String accountStatusType, Timestamp createdDate) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.balance = balance;
        this.accountType = accountType;
        this.accountStatusType = accountStatusType;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountStatusType() {
        return accountStatusType;
    }

    public void setAccountStatusType(String accountStatusType) {
        this.accountStatusType = accountStatusType;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getId().equals(account.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", description='" + description + '\'' +
                ", balance=" + balance +
                ", accountType=" + accountType +
                ", accountStatusType=" + accountStatusType +
                ", createdDate=" + createdDate +
                '}';
    }
}
