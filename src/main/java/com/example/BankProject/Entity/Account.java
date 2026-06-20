package com.example.BankProject.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.BankProject.ENUM.AccountStatus;
import com.example.BankProject.ENUM.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;


@Entity
@Table(name = "bankaccounts")
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long accountId;
	
	@Column(unique = true, nullable = false)
	private String accountNumber;
	
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	
	@Column(nullable = false)
	private BigDecimal balance;
	
	private String branchName;
	
	private String ifscCode;
	
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
	
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	
	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
		status = AccountStatus.ACTIVE;
	}
	
	//constructor
	public Account() {
		
	}

	public Account(Long accountId, String accountNumber, AccountType accountType, BigDecimal balance, String branchName,
			String ifscCode, AccountStatus status, LocalDateTime createdAt, Customer customer) {
		super();
		this.accountId = accountId;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.balance = balance;
		this.branchName = branchName;
		this.ifscCode = ifscCode;
		this.status = status;
		this.createdAt = createdAt;
		this.customer = customer;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
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
	
	
	
	
	
	
	
	
}
	
