package com.example.BankProject.OfficerDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateOfficerRequestDTO {
	
//	@NotBlank(message = "Name is Required")
	private String name;
	
	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is requried")
	private String email;
	
	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "password must be at least 6 Characters")
	private String password;
	
	private String phone;
	
	@NotNull(message ="Role is required")
	private String role;
	
	public CreateOfficerRequestDTO() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public CreateOfficerRequestDTO(String name, String email, String phone, String role,String password) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.role = role;
		this.password=password;
	}
	
	

}
