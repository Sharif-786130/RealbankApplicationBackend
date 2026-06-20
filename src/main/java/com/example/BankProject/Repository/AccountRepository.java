package com.example.BankProject.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.BankProject.Entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

//	Optional<Account> findByCustomerId(Long customerId);
	
	Optional<Account> findByAccountNumber(String accountNumber);
	
	List<Account> findByCustomerCustomerId(Long customerId);
	
	
	
	boolean existsByCustomerCustomerId(Long customerId);
	
	@Query("SELECT COALESCE(SUM(a.balance), 0) FROM Account a")
	BigDecimal getTotalBalance();
	
	



}
