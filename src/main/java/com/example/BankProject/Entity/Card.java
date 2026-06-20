package com.example.BankProject.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.BankProject.ENUM.CardStatus;
import com.example.BankProject.ENUM.CardType;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class Card {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cardId;

	@Column(unique = true, nullable = false)
	private String cardNumber;

	@Enumerated(EnumType.STRING)
	private CardType cardType;

	@Enumerated(EnumType.STRING)
	private CardStatus status;

	private String cvv;

	private LocalDate expiryDate;

	private LocalDateTime issuedAt;

	private Long issuedByOfficerId;

	@ManyToOne
	@JoinColumn(name = "account_id", nullable = false)
	private Account account;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@PrePersist
	public void prePersist() {
		issuedAt = LocalDateTime.now();
		status = CardStatus.ACTIVE;
		expiryDate = LocalDate.now().plusYears(5);
	}

	// ── Getters & Setters ─────────────────────────────────────────────────────

	public Long getCardId() {
		return cardId;
	}

	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public CardType getCardType() {
		return cardType;
	}

	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public CardStatus getStatus() {
		return status;
	}

	public void setStatus(CardStatus status) {
		this.status = status;
	}

	public String getCvv() {
		return cvv;
	}

	public void setCvv(String cvv) {
		this.cvv = cvv;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public LocalDateTime getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(LocalDateTime issuedAt) {
		this.issuedAt = issuedAt;
	}

	public Long getIssuedByOfficerId() {
		return issuedByOfficerId;
	}

	public void setIssuedByOfficerId(Long id) {
		this.issuedByOfficerId = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}