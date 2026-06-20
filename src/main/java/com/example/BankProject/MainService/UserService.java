package com.example.BankProject.MainService;

import com.example.BankProject.DTO.CreateUserDTO;
import com.example.BankProject.Entity.User;

public interface UserService {
	
	User createUser(CreateUserDTO dto);
	
	User getByEmail(String email);
	
	User authenticate(String email, String rawPassword);

}
