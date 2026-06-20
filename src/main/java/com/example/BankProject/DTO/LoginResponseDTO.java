package com.example.BankProject.DTO;

public class LoginResponseDTO {
	
	private String accessToken;
	
	private String refreshToken;
	
	private String role;
	
	private Boolean passwordResetRequired;
	
	private Long customerId; 



	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	

	public Boolean getPasswordResetRequired() {
		return passwordResetRequired;
	}

	public void setPasswordResetRequried(Boolean passwordResetRequired) {
		this.passwordResetRequired = passwordResetRequired;
	}
	
	

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public LoginResponseDTO( String accessToken, String refreshToken, String role, Boolean passwordResetRequired,Long customerId) {
		super();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.role = role;
		this.passwordResetRequired=passwordResetRequired;
		this.customerId = customerId; 
	}
	
	
	
	

	

}
