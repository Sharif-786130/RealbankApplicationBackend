package com.example.BankProject.OfficerDTO;

public class UpdateOfficerRequestDTO {
	
	private String name;
	
	private String phone;
	
	private String email;
	
	private String password;
	
	private Boolean active;
	
	
	public UpdateOfficerRequestDTO() {
		
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public UpdateOfficerRequestDTO(String name, String phone, Boolean active,String email,String password) {
		super();
		this.name = name;
		this.phone = phone;
		this.active = active;
		this.email=email;
		this.password=password;
	}
	
	

}
