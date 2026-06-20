package com.example.BankProject.Entity;

import java.time.LocalDateTime;

import com.example.BankProject.ENUM.TicketStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ticketId;

	@Column(unique = true, nullable = false)
	private String ticketNumber;

	@Column(nullable = false)
	private String subject;

	@Column(nullable = false, length = 1000)
	private String description;

	@Enumerated(EnumType.STRING)
	private TicketStatus status;

	private String resolution;

	private Long customerId;

	private String customerName;

	private Long resolvedByOfficerId;

	private LocalDateTime createdAt;

	private LocalDateTime resolvedAt;

	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
		status = TicketStatus.OPEN;
		ticketNumber = "TKT-" + System.currentTimeMillis();
	}

	// ── Getters & Setters ─────────────────────────────────────────────────────

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TicketStatus getStatus() {
		return status;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getResolvedByOfficerId() {
		return resolvedByOfficerId;
	}

	public void setResolvedByOfficerId(Long id) {
		this.resolvedByOfficerId = id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getResolvedAt() {
		return resolvedAt;
	}

	public void setResolvedAt(LocalDateTime resolvedAt) {
		this.resolvedAt = resolvedAt;
	}
}