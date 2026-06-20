package com.example.BankProject.AdminDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateAdminRequestDTO {
	
	private String name;
	
	@Email(message="Invalid email format")
	@NotNull(message = "Email is Requried")
	private String email;
	
	@NotNull(message = "Password is Required")
	@Size(min=6,message = "password must be at least 6 Characters")
	private String password;
	
	private String phone;
	
	public CreateAdminRequestDTO() {
		
	}

	public CreateAdminRequestDTO(String name,String email, String password,String phone) {
		super();
		this.name=name;
		this.email = email;
		this.password = password;
		this.phone=phone;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	

}
