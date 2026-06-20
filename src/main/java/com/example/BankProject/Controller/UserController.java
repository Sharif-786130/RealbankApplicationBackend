package com.example.BankProject.Controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.BankProject.Entity.User;
import com.example.BankProject.Mapper.UserMapper;
import com.example.BankProject.UserDTO.UserResponseDTO;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@GetMapping("/profile")
	public UserResponseDTO profile(Authentication auth) {
		return UserMapper.toDTO((User) auth.getPrincipal());
	}

}
