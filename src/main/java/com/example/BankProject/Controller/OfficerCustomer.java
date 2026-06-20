package com.example.BankProject.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankProject.CustomerDTO.CreateCustomerRequestDTO;
import com.example.BankProject.CustomerDTO.CustomerDetailsResponseDTO;
import com.example.BankProject.CustomerDTO.UpdateCustomerRequestDTO;
import com.example.BankProject.CustomerDTO.UpdateCustomerResponseDTO;
import com.example.BankProject.Entity.Customer;
import com.example.BankProject.MainService.CustomerService;
import com.example.BankProject.Mapper.CustomerMapper;
import com.example.BankProject.Security.CustomUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "8. Officer — Customers", description = "Officer onboards and manages customers in their branch")
@RestController
@RequestMapping("/api/officer/customers")
@PreAuthorize("hasRole('OFFICER')")
public class OfficerCustomer {
	

	private final CustomerService customerService;
	
	public OfficerCustomer(CustomerService customerService) {
		this.customerService = customerService;
	}
	
	//CREATE CUSTOMER
	@Operation(summary = "Create customer", description = "Officer registers a new customer in their branch.")
	@PostMapping
	public ResponseEntity<Customer> createCustomer(
			@RequestBody CreateCustomerRequestDTO dto,
			@AuthenticationPrincipal CustomUserDetails userDetails){
		
		Customer customer = CustomerMapper.convertToEntity(dto);
		
		Customer savedCustomer = customerService.createCustomer
				(customer,
				 userDetails.getId(),
				 userDetails.getBranchCode());
		System.out.println("USER DETAILS = " + userDetails);
		return ResponseEntity.ok(savedCustomer);
		
	}
	
	//Get CUSTOMER BY ID(Branch restricted)
	@Operation(summary = "Get customer by ID", description = "Officer fetches a customer — restricted to their own branch.")
	@GetMapping("/{id}")
	public ResponseEntity<CustomerDetailsResponseDTO> getCustomer(
			@PathVariable Long id,
			@AuthenticationPrincipal CustomUserDetails userDetails){
		
		CustomerDetailsResponseDTO customer =customerService.getCustomerById
				(id,
				 userDetails.getBranchCode());
		
		return ResponseEntity.ok(customer);
		
	}
	
	//GET ALL CUSTOMER OF BRANCH
	@Operation(summary = "Get all branch customers", description = "Officer sees all customers in their branch.")
	@GetMapping
	public ResponseEntity<List<Customer>> getCustomsByBranch(
			@AuthenticationPrincipal CustomUserDetails userDetails){
		
		return ResponseEntity.ok
				(customerService.getCustomersByBranch(userDetails.getBranchCode()));
	}
	
	@Operation(summary = "Update customer", description = "Officer updates a customer's basic details.")
	@PutMapping("/{id}")
	public ResponseEntity<UpdateCustomerResponseDTO> updateCustomer(
			@PathVariable Long id,
			@RequestBody UpdateCustomerRequestDTO dto,
			@AuthenticationPrincipal CustomUserDetails userDetails){
		
		UpdateCustomerResponseDTO response = 
				customerService.updateCustomerBasicDetails(id,
				 dto,
				 userDetails.getBranchCode());
		
		return ResponseEntity.ok(response);
	}
	
	

}
