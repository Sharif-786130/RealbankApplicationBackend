package com.example.BankProject.TransactionDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDTO {
    private Long transactionId;
    private String transactionNumber;
    private BigDecimal amount;
    private String type;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String description;
    private String status;
    private LocalDateTime createdAt;

    public TransactionResponseDTO(Long transactionId, String transactionNumber,
            BigDecimal amount, String type, BigDecimal balanceBefore,
            BigDecimal balanceAfter, String description,
            String status, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.transactionNumber = transactionNumber;
        this.amount = amount;
        this.type = type;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getTransactionId() { return transactionId; }
    public String getTransactionNumber() { return transactionNumber; }
    public BigDecimal getAmount() { return amount; }
    public String getType() { return type; }
    public BigDecimal getBalanceBefore() { return balanceBefore; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
