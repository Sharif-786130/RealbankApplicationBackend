package com.example.BankProject.AccountDTO;

import java.math.BigDecimal;

public class AccountRequestDTO {
	
	private Long customerId;
	
	private String accountType;
	
	private BigDecimal initialDeposit;
	
	private String branchName;
	
	private String ifscCode;
	
	
	//constructor
	public AccountRequestDTO() {
		
	}

	public AccountRequestDTO(Long customerId, String accountType, BigDecimal initialDeposit, String branchName,
			String ifscCode) {
		super();
		this.customerId = customerId;
		this.accountType = accountType;
		this.initialDeposit = initialDeposit;
		this.branchName = branchName;
		this.ifscCode = ifscCode;
	}

	
	//getters and setters
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getInitialDeposit() {
		return initialDeposit;
	}

	public void setInitialDeposit(BigDecimal initialDeposit) {
		this.initialDeposit = initialDeposit;
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
	
	

}
