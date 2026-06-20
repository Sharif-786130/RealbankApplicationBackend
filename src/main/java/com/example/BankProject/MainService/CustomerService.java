package com.example.BankProject.MainService;

import java.util.List;

import com.example.BankProject.CustomerDTO.CreateCustomerResponseDTO;
import com.example.BankProject.CustomerDTO.CustomerDetailsResponseDTO;
import com.example.BankProject.CustomerDTO.UpdateCustomerRequestDTO;
import com.example.BankProject.CustomerDTO.UpdateCustomerResponseDTO;
import com.example.BankProject.Entity.Customer;

public interface CustomerService {
	
	Customer createCustomer(Customer customer,Long officerId,String branchCode);
	
	CustomerDetailsResponseDTO getCustomerById(Long customerId, String branchcode);
	
	
//	Customer getCustomerById(Long customerId,String branchCode);
	
	List<Customer> getCustomersByBranch(String branchCode);
	
//	Customer updateCustomerBasicDetails(
//			Long customerId,
//			Customer updatedCustomer,
//			String branchCode
//		);
	
	UpdateCustomerResponseDTO updateCustomerBasicDetails(
			Long customerId,
			UpdateCustomerRequestDTO request,
			String branchCode
		);
	
	void deactivatedCustomer(Long customerId);
	
	CreateCustomerResponseDTO getCustomerById(Long id);
	
	void resetPassword(Long userId, String newPassword);

}
