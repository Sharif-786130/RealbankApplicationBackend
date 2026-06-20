package com.example.BankProject.MainService;

import java.util.List;

import com.example.BankProject.AdminDTO.CreateAdminRequestDTO;
import com.example.BankProject.Entity.User;

public interface SuperAdminService {
	
	User createAdmin(CreateAdminRequestDTO dto);
	
	List<User> getAllAdmins();
	
	User getAdminById(Long id);
	
	void deleteAdmin(Long id);

}
