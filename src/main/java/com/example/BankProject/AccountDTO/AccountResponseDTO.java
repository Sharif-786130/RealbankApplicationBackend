package com.example.BankProject.AccountDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponseDTO {
	
	private Long accountId;
	
	private String accountNumber;
	
	private String accountType;
	
	private BigDecimal balance;
	
	private String status;
	
	private String branchName;
	
	private String ifscCode;
	
	private LocalDateTime createdAt;
	
	//constructor
	public AccountResponseDTO() {
		
	}

	public AccountResponseDTO(Long accountId, String accountNumber, String accountType, BigDecimal balance,
			String status, String branchName, String ifscCode, LocalDateTime createdAt) {
		super();
		this.accountId = accountId;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.balance = balance;
		this.status = status;
		this.branchName = branchName;
		this.ifscCode = ifscCode;
		this.createdAt = createdAt;
	}

	//setters and getters
	
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

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	

	

}
