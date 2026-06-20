package com.example.BankProject.LoanDTO;

import java.math.BigDecimal;

public class LoanRequestDTO {
	
	private Long customerId;
	private String loanType;
	private BigDecimal loanAmount;
	private Double intrestRate;
	private Integer tenureMonths;
	
	public LoanRequestDTO() {
		
	}

	public LoanRequestDTO(Long customerId, String loanType, BigDecimal loanAmount, Double intrestRate,
			Integer tenureMonths) {
		super();
		this.customerId = customerId;
		this.loanType = loanType;
		this.loanAmount = loanAmount;
		this.intrestRate = intrestRate;
		this.tenureMonths = tenureMonths;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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

	public Double getIntrestRate() {
		return intrestRate;
	}

	public void setIntrestRate(Double intrestRate) {
		this.intrestRate = intrestRate;
	}

	public Integer getTenureMonths() {
		return tenureMonths;
	}

	public void setTenureMonths(Integer tenureMonths) {
		this.tenureMonths = tenureMonths;
	}
	
	

}
