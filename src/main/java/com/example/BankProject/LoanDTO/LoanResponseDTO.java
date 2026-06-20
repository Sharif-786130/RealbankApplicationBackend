package com.example.BankProject.LoanDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LoanResponseDTO {

    private Long loanId;
    private String loanNumber;
    private String loanType;
    private BigDecimal loanAmount;
    private BigDecimal emiAmount;
    private BigDecimal outstandingBalance;   // fixed: lowercase 's' → consistent JSON key
    private String status;
    private Integer tenureMonths;
    private Double interestRate;             // NEW: frontend needs this for EMI preview
    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;        // NEW: used as startDate for clearance date calc
    private LocalDateTime disbursedAt;       // NEW: disbursement date

    public LoanResponseDTO() {}

    public LoanResponseDTO(Long loanId, String loanNumber, String loanType,
                           BigDecimal loanAmount, BigDecimal emiAmount,
                           BigDecimal outstandingBalance, String status,
                           Integer tenureMonths, Double interestRate,
                           LocalDateTime createdAt, LocalDateTime approvedAt,
                           LocalDateTime disbursedAt) {
        this.loanId = loanId;
        this.loanNumber = loanNumber;
        this.loanType = loanType;
        this.loanAmount = loanAmount;
        this.emiAmount = emiAmount;
        this.outstandingBalance = outstandingBalance;
        this.status = status;
        this.tenureMonths = tenureMonths;
        this.interestRate = interestRate;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
        this.disbursedAt = disbursedAt;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────

    public Long getLoanId() { return loanId; }
    public void setLoanId(Long loanId) { this.loanId = loanId; }

    public String getLoanNumber() { return loanNumber; }
    public void setLoanNumber(String loanNumber) { this.loanNumber = loanNumber; }

    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }

    public BigDecimal getLoanAmount() { return loanAmount; }
    public void setLoanAmount(BigDecimal loanAmount) { this.loanAmount = loanAmount; }

    public BigDecimal getEmiAmount() { return emiAmount; }
    public void setEmiAmount(BigDecimal emiAmount) { this.emiAmount = emiAmount; }

    public BigDecimal getOutstandingBalance() { return outstandingBalance; }
    public void setOutstandingBalance(BigDecimal outstandingBalance) {
        this.outstandingBalance = outstandingBalance;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }

    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public LocalDateTime getDisbursedAt() { return disbursedAt; }
    public void setDisbursedAt(LocalDateTime disbursedAt) { this.disbursedAt = disbursedAt; }
}