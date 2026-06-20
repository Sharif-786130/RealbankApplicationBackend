package com.example.BankProject.DTO;

import com.example.BankProject.ENUM.Role;

public class CreateUserDTO {
	private String email;
	
	private String password;
	
	private Role role;
	
	public CreateUserDTO() {
		
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

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public CreateUserDTO(String email, String password, Role role) {
		super();
		this.email = email;
		this.password = password;
		this.role = role;
	}
	
	

}
