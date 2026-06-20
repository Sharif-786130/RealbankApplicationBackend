package com.example.BankProject.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BankProject.Entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {

	List<Card> findByCustomerCustomerId(Long customerId);

	List<Card> findByAccountAccountId(Long accountId);

	boolean existsByAccountAccountId(Long accountId);
}