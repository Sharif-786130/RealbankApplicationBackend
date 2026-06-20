package com.example.BankProject.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.BankProject.Entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	Optional<Customer> findByEmail(String email);
	
	List<Customer> findByBranchCode(String branchCode);
	
	Optional<Customer> findByCustomerIdAndBranchCode(Long customerId,String branchCode);
	
	Optional<Customer> findByUserId(Long userId);
	
//	Optional<Customer> findByUserEmail(String email);
	
	long countByIsActive(Boolean isActive);
	
	Optional<Customer> findByMobileNumber(String mobileNumber);
	
	long count();
	
	@Query("SELECT c FROM Customer c WHERE c.branchCode = :branchCode OR c.branchCode IS NULL")
	List<Customer> findByBranchCodeOrBranchCodeIsNull(@Param("branchCode") String branchCode);

	@Query("SELECT c FROM Customer c WHERE c.customerId = :customerId AND (c.branchCode = :branchCode OR c.branchCode IS NULL)")
	Optional<Customer> findByCustomerIdAccessibleToBranch(@Param("customerId") Long customerId, @Param("branchCode") String branchCode);


}
