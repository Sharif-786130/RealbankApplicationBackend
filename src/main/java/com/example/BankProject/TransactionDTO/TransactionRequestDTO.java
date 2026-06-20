package com.example.BankProject.TransactionDTO;

import com.example.BankProject.ENUM.TransactionType;

public class TransactionRequestDTO {

	private Double amount;
	
	private TransactionType type;
	
	private String upiId; // only for UPI
	
	private String ifscCode; // only for NEFT
	
	public TransactionRequestDTO() {
		
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}
	
	

	public String getUpiId() {
		return upiId;
	}

	public void setUpiId(String upiId) {
		this.upiId = upiId;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public TransactionRequestDTO(Double amount, TransactionType type, String upiId, String ifscCode) {
		super();
		this.amount = amount;
		this.type = type;
		this.upiId = upiId;
		this.ifscCode = ifscCode;
	}

	
	
	
}
