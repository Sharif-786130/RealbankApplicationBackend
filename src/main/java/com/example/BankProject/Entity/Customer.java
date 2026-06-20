package com.example.BankProject.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.BankProject.ENUM.KycStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long customerId;
	
	//Personal Info
	private String firstName;
	private String lastName;
	private String gender;
	
	@Column(name = "date_of_birth")
	private LocalDate dateofBirth;
	
	@Column(nullable = false) 
	private String email;
	
//	@Column(nullable = false)
//	private String password;
	
	private Long userId;
	
	@Column(unique = true,nullable=false)
	private String mobileNumber;
	
	//Address
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String pincode;
	private String country;
	
	//KYC
	@Column(unique = true)
	private String aadhaarNumber;
	
	@Column(unique = true)
	private String panNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private KycStatus kycStatus;
	
	//Bank mapping
	@Column(unique = true)
	private String customerNumber;
	
//	@Column(nullable = false)
//	private boolean passwordResetRequired;
	
	private String branchCode;
	
	//Status
	@Column(name = "is_active",nullable=false)
	private Boolean isActive = true;
	
	
	//Audit
	@Column(nullable = false)
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	//Audit
	private Long createdByOfficerId;
	
	@PrePersist
	public void onCreate() {
		this.createdAt = LocalDateTime.now();
		this.kycStatus = KycStatus.PENDING;
	}
	
	@PreUpdate
	public void onUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	
	//Constructors
	public Customer() {
		
	}

	public Customer(Long customerId, String firstName, String lastName, String gender, LocalDate dateofBirth, String email,
			String mobileNumber,Long userId, String addressLine1, String addressLine2, String city, String state, String pincode,
			String country, String aadhaarNumber, String panNumber, KycStatus kycStatus, String customerNumber,
			String branchCode, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt,
			Long createdByOfficerId) {
		super();
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateofBirth = dateofBirth;
		this.email = email;
	

		this.userId=userId;
		this.mobileNumber = mobileNumber;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
		this.country = country;
		this.aadhaarNumber = aadhaarNumber;
		this.panNumber = panNumber;
		this.kycStatus = kycStatus;
		this.customerNumber = customerNumber;
		this.branchCode = branchCode;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.createdByOfficerId = createdByOfficerId;
	}

	//Getters and Setters
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

//	public boolean isPasswordResetRequired() {
//		return passwordResetRequired;
//	}
//
//	public void setPasswordResetRequired(boolean passwordResetRequired) {
//		this.passwordResetRequired = passwordResetRequired;
//	}

	
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}

	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public KycStatus getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(KycStatus kycStatus) {
		this.kycStatus = kycStatus;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public Boolean isActive() {
		return isActive;
	}

	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getCreatedByOfficerId() {
		return createdByOfficerId;
	}

	public void setCreatedByOfficerId(Long createdByOfficerId) {
		this.createdByOfficerId = createdByOfficerId;
	}
	
	
	
	

}
