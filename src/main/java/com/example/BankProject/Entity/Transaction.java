package com.example.BankProject.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.BankProject.ENUM.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private String transactionNumber;

    //  Changed Double to BigDecimal
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType Type;

    private BigDecimal balanceBefore;

    private BigDecimal balanceAfter;

    private String description;

    private Long performedById;

    private LocalDateTime createdAt;

    private String status;
    
    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;  // ← add this

    private String upiId;       // ← for UPI transactions
    private String ifscCode;    // ← for NEFT/IMPS

    // add getters and setters
    public Account getToAccount() { return toAccount; }
    public void setToAccount(Account toAccount) { this.toAccount = toAccount; }

    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = "SUCCESS";
        this.transactionNumber = "TXN" + System.currentTimeMillis();
    }

    public Transaction() {}

    // ✅ Getters and Setters
    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public String getTransactionNumber() { return transactionNumber; }
    public void setTransactionNumber(String transactionNumber) { this.transactionNumber = transactionNumber; }

    // ✅ BigDecimal amount
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public TransactionType getType() { return Type; }
    public void setType(TransactionType type) { Type = type; }

    public BigDecimal getBalanceBefore() { return balanceBefore; }
    public void setBalanceBefore(BigDecimal balanceBefore) { this.balanceBefore = balanceBefore; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public void setBalanceAfter(BigDecimal balanceAfter) { this.balanceAfter = balanceAfter; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getPerformedById() { return performedById; }
    public void setPerformedById(Long performedById) { this.performedById = performedById; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
}