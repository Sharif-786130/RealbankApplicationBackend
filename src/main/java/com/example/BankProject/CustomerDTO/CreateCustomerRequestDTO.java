package com.example.BankProject.CustomerDTO;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateCustomerRequestDTO {
	
	@NotBlank(message = "First name is requried")
	private String firstName;
	
	@NotBlank(message ="Last name is requried")
	private String lastName;
	
	private String gender;
	
	private LocalDate dateofBirth;
	
	@Email(message = "Invalid email format")
	@NotBlank(message ="Email is requried")
	private String email;
	
	@NotBlank(message = "Mobile Number is requried")
	@Pattern(regexp ="^[6-9]\\d{9}$",
	message =" Invalid mobile number")
	private String mobileNumber;
	
	@NotBlank(message = "AddreessLine1 is requried")
	private String addressLine1;
	
	private String addressLine2;
	
	private String city;
	
	private String state;
	
	private String pincode;
	
	private String country;
	
	@NotBlank(message ="Aadhaar number is requried")
	@Pattern(regexp = "\\d{12}",message = "Invalid Aadhaar number")
	private String aadhaarNumber;
	
	private String panNumber;
	
	private Boolean active;
	
	public CreateCustomerRequestDTO() {
		
	}

	public CreateCustomerRequestDTO(String firstName, String lastName,String gender,LocalDate dateofBirth, String email, String mobileNumber,
			String addressLine1, String addressLine2, String city, String state, String pincode, String country,
			String aadhaarNumber, String panNumber,Boolean active) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.dateofBirth = dateofBirth;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
		this.country = country;
		this.aadhaarNumber = aadhaarNumber;
		this.panNumber = panNumber;
		this.active = active;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	
	

}
