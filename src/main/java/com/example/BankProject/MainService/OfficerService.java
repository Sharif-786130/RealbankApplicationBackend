package com.example.BankProject.MainService;

import java.util.List;

import com.example.BankProject.Entity.User;
import com.example.BankProject.OfficerDTO.CreateOfficerRequestDTO;
import com.example.BankProject.OfficerDTO.UpdateOfficerRequestDTO;

public interface OfficerService {
	
	User createOfficer(CreateOfficerRequestDTO dto);
	
	List<User> getAllOfficers();
	
	User getOfficerById(Long id);
	
	List<User> getOfficerByName(String name);
	
	User updateOfficer(Long id, UpdateOfficerRequestDTO dto);
	
	void deleteOfficer(Long id);

}
