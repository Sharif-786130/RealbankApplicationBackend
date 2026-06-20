package com.example.BankProject.OfficerDTO;

public class UpdateOfficerResponseDTO {
	
	private String message;
	
	private Long officerId;
	
	private boolean active;
	
	public UpdateOfficerResponseDTO() {
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getOfficerId() {
		return officerId;
	}

	public void setOfficerId(Long officerId) {
		this.officerId = officerId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public UpdateOfficerResponseDTO(String message, Long officerId, boolean active) {
		super();
		this.message = message;
		this.officerId = officerId;
		this.active = active;
	}
	

}
