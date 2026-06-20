package com.example.BankProject.Entity;

import java.time.LocalDateTime;
import com.example.BankProject.ENUM.Role;
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
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private Boolean passwordResetRequired;

	private String phone;

	private String branchCode;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	@Column(name = "employee_id", unique = true, updatable = false)
	private Long employeeId;

	private boolean active = true;

	@Column(name = "otp_code")
	private String otpCode;

	@Column(name = "otp_expiry")
	private LocalDateTime otpExpiry;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	// ----------------------------LIFECYCLE HOOKS---------------
	@PrePersist
	public void prepersist() {

		if (this.createdAt == null) {
			this.createdAt = LocalDateTime.now();
		}

		if (this.role == Role.OFFICER && this.employeeId == null) {
			this.employeeId = System.currentTimeMillis();
		}
	}

	@PreUpdate
	public void preupdate() {
		if (this.role == Role.OFFICER && this.employeeId == null) {
			this.employeeId = System.currentTimeMillis();
		}

		// If role changed from Officer to something else
		if (this.role != Role.OFFICER) {
			this.employeeId = null;
		}
	}

	// constructor
	public User() {

	}

	// getters and setters
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getPasswordResetRequired() {
		return passwordResetRequired;
	}

	public void setPasswordResetRequired(Boolean passwordResetRequired) {
		this.passwordResetRequired = passwordResetRequired;
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

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
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

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getOtpCode() {
		return otpCode;
	}

	public void setOtpCode(String otpCode) {
		this.otpCode = otpCode;
	}

	public LocalDateTime getOtpExpiry() {
		return otpExpiry;
	}

	public void setOtpExpiry(LocalDateTime otpExpiry) {
		this.otpExpiry = otpExpiry;
	}

	// constructor and super constructor
	public User(Long id, String email, String password, Boolean passwordResetRequired, Role role, String branchCode,
			boolean active, LocalDateTime createdAt, String name, String phone, Long employeeId) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.passwordResetRequired = passwordResetRequired;
		this.role = role;
		this.active = active;
		this.branchCode = branchCode;
		this.createdAt = createdAt;
		this.name = name;
		this.phone = phone;
		this.employeeId = employeeId;
	}

}