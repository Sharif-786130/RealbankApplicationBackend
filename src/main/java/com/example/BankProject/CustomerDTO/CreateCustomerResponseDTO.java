package com.example.BankProject.CustomerDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateCustomerResponseDTO {
	
	private Long customerId;
	
	private String customerNumber;
	private String firstName;
	private String lastName;
	private String gender;
	private LocalDate dateofBirth;
	
	private String email;
	private String mobileNumber;
	
	private String aadhaarNumber;
	private String panNumber;
	
	private String KycStatus;
	private boolean active;
	
	private String branchCode;
	private Long createdByOfficerId;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	private boolean passwordResetRequired;
	
	
	public CreateCustomerResponseDTO() {
		
	}

	public CreateCustomerResponseDTO(Long customerId, String customerNumber, String firstName, String lastName,String gender,LocalDate dateofBirth, String email,
			String mobileNumber,String aadhaarNumber,String panNumber, String kycStatus, boolean active,String branchCode,
			Long createdByOfficerId,LocalDateTime createdAt,LocalDateTime updatedAt, boolean passwordResetRequired) {
		super();
		this.customerId = customerId;
		this.customerNumber = customerNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateofBirth = dateofBirth;
		this.aadhaarNumber = aadhaarNumber;
		this.panNumber = panNumber;
		this.email = email;
		this.mobileNumber = mobileNumber;
		KycStatus = kycStatus;
		this.active = active;
		this.createdByOfficerId = createdByOfficerId;
		this.updatedAt = updatedAt;
		this.branchCode = branchCode;
		this.createdAt = createdAt;
		this.passwordResetRequired=passwordResetRequired;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getDateofBirth() {
		return dateofBirth;
	}

	public void setDateofBirth(LocalDate dateofBirth) {
		this.dateofBirth = dateofBirth;
	}

	public String getAadhaarNumber() {
		return aadhaarNumber;
	}

	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public Long getCreatedByOfficerId() {
		return createdByOfficerId;
	}

	public void setCreatedByOfficerId(Long createdByOfficerId) {
		this.createdByOfficerId = createdByOfficerId;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getKycStatus() {
		return KycStatus;
	}

	public void setKycStatus(String kycStatus) {
		KycStatus = kycStatus;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isPasswordResetRequired() {
		return passwordResetRequired;
	}

	public void setPasswordResetRequired(boolean passwordResetRequired) {
		this.passwordResetRequired = passwordResetRequired;
	}
	
	

}
