package com.example.BankProject.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.BankProject.ENUM.LoanStatus;

import jakarta.persistence.Column;
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
@Table(name="loans")
public class Loan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long loanId;
	
	@Column(unique = true,nullable = false)
	private String loanNumber;
	
	private String loanType;  //HOME, PERSONAL, VEHICLE
	
	private BigDecimal loanAmount;
	
	
	private Double interstRate;
	
	private Integer tenureMonths;
	
	private BigDecimal emiAmount;
	
	private BigDecimal outstandingBalance;
	
	@Enumerated(EnumType.STRING)
	private LoanStatus status;
	
	private LocalDateTime disbursedAt;
	
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
//		this.disbursedAt = LocalDateTime.now();
//		this.status = LoanStatus.ACTIVE;
	}
	@ManyToOne
	@JoinColumn(name = "officer_id")
	private User officer;
	
	@ManyToOne
	@JoinColumn(name = "approved_by")
	private User approvedBy;
	
	private LocalDateTime approvedAt;
	
	public Loan() {
		
	}

//	public Loan(Long loanId, String loanNumber, String loanType, BigDecimal loanAmount, Double interstRate,
//			Integer tenureMonths, BigDecimal outstandingBalance, LoanStatus status, LocalDateTime disbursedAt,
//			LocalDateTime createdAt, Customer customer,BigDecimal emiAmount) {
//		super();
//		this.loanId = loanId;
//		this.loanNumber = loanNumber;
//		this.loanType = loanType;
//		this.loanAmount = loanAmount;
//		this.interstRate = interstRate;
//		this.tenureMonths = tenureMonths;
//		this.outstandingBalance = outstandingBalance;
//		this.status = status;
//		this.disbursedAt = disbursedAt;
//		this.createdAt = createdAt;
//		this.customer = customer;
//		this.emiAmount = emiAmount;
//	}
	

	public Loan(Long loanId, String loanNumber, String loanType, BigDecimal loanAmount, Double interstRate,
			Integer tenureMonths, BigDecimal emiAmount, BigDecimal outstandingBalance, LoanStatus status,
			LocalDateTime disbursedAt, LocalDateTime createdAt, Customer customer, User officer, User approvedBy,
			LocalDateTime approvedAt) {
		super();
		this.loanId = loanId;
		this.loanNumber = loanNumber;
		this.loanType = loanType;
		this.loanAmount = loanAmount;
		this.interstRate = interstRate;
		this.tenureMonths = tenureMonths;
		this.emiAmount = emiAmount;
		this.outstandingBalance = outstandingBalance;
		this.status = status;
		this.disbursedAt = disbursedAt;
		this.createdAt = createdAt;
		this.customer = customer;
		this.officer = officer;
		this.approvedBy = approvedBy;
		this.approvedAt = approvedAt;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public String getLoanNumber() {
		return loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Double getInterstRate() {
		return interstRate;
	}

	public void setInterstRate(Double interstRate) {
		this.interstRate = interstRate;
	}

	public Integer getTenureMonths() {
		return tenureMonths;
	}

	public void setTenureMonths(Integer tenureMonths) {
		this.tenureMonths = tenureMonths;
	}

	public BigDecimal getOutstandingBalance() {
		return outstandingBalance;
	}

	public void setOutstandingBalance(BigDecimal outstandingBalance) {
		this.outstandingBalance = outstandingBalance;
	}

	public LoanStatus getStatus() {
		return status;
	}

	public void setStatus(LoanStatus status) {
		this.status = status;
	}

	public LocalDateTime getDisbursedAt() {
		return disbursedAt;
	}

	public void setDisbursedAt(LocalDateTime disbursedAt) {
		this.disbursedAt = disbursedAt;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public BigDecimal getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(BigDecimal emiAmount) {
		this.emiAmount = emiAmount;
	}

	public User getOfficer() {
		return officer;
	}

	public void setOfficer(User officer) {
		this.officer = officer;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LocalDateTime getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(LocalDateTime approvedAt) {
		this.approvedAt = approvedAt;
	}
	
    	
	
	
	

}
