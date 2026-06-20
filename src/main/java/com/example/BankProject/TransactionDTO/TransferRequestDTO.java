package com.example.BankProject.TransactionDTO;

import java.math.BigDecimal;

public class TransferRequestDTO {
    private Long fromAccountId;
    private String toAccountNumber;  // receiver enters account number
    private BigDecimal amount;
    private String type;  // NEFT, IMPS, UPI
    private String upiId; // only for UPI
    private String ifscCode; // only for NEFT
	public Long getFromAccountId() {
		return fromAccountId;
	}
	public void setFromAccountId(Long fromAccountId) {
		this.fromAccountId = fromAccountId;
	}
	public String getToAccountNumber() {
		return toAccountNumber;
	}
	public void setToAccountNumber(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
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
	public TransferRequestDTO(Long fromAccountId, String toAccountNumber, BigDecimal amount, String type, String upiId,
			String ifscCode) {
		super();
		this.fromAccountId = fromAccountId;
		this.toAccountNumber = toAccountNumber;
		this.amount = amount;
		this.type = type;
		this.upiId = upiId;
		this.ifscCode = ifscCode;
	}
    
    

    
}
