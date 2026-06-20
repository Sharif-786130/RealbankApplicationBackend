package com.example.BankProject.Mapper;

import com.example.BankProject.CustomerDTO.CreateCustomerRequestDTO;
import com.example.BankProject.Entity.Customer;

public class CustomerMapper {
	
	private CustomerMapper() {
		
	}
	
	public static Customer convertToEntity(CreateCustomerRequestDTO dto) {
		Customer customer = new Customer();
		
		customer.setFirstName(dto.getFirstName());
		customer.setLastName(dto.getLastName());
		customer.setGender(dto.getGender());
		customer.setDateofBirth(dto.getDateofBirth());
		customer.setEmail(dto.getEmail());
		customer.setMobileNumber(dto.getMobileNumber());
		customer.setAddressLine1(dto.getAddressLine1());
		customer.setAddressLine2(dto.getAddressLine2());
		customer.setCity(dto.getCity());
		customer.setState(dto.getState());
		customer.setCountry(dto.getCountry());
		customer.setPincode(dto.getPincode());
		customer.setAadhaarNumber(dto.getAadhaarNumber());
		customer.setPanNumber(dto.getPanNumber());
		
		return customer;
	}

}
