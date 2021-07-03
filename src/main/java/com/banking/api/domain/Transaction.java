package com.banking.api.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

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

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Long transactionDate) {
        this.transactionDate = transactionDate;
    }
}
