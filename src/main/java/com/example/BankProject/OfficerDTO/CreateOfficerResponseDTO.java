package com.example.BankProject.OfficerDTO;

public class CreateOfficerResponseDTO {
	
	private String message;
	
	private Long employeeId;
	
	public CreateOfficerResponseDTO() {
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public CreateOfficerResponseDTO(String message, Long employeeId) {
		super();
		this.message = message;
		this.employeeId = employeeId;
	}
	
	

}
