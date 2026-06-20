package com.example.BankProject.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BankProject.Entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanRepository extends JpaRepository<Loan, Long>{
	
	List<Loan> findByCustomerCustomerId(Long customerId);
	
	Page<Loan> findByCustomerCustomerId(Long customerId, Pageable pageable);
	
	

}
