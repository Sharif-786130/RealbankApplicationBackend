package com.example.BankProject.UserDTO;

import java.time.LocalDateTime;

import com.example.BankProject.ENUM.Role;

public class UserResponseDTO {
	
	private Long id;
	
	private String email;
	
	private Role role;
	
	private boolean active;
	
	private LocalDateTime createdAt;
	
	public UserResponseDTO() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public UserResponseDTO(Long id, String email, Role role, boolean active, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.email = email;
		this.role = role;
		this.active = active;
		this.createdAt = createdAt;
	}
	
	
	

}
