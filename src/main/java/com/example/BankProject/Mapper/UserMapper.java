package com.example.BankProject.Mapper;

import java.time.LocalDateTime;

import com.example.BankProject.DTO.CreateUserDTO;
import com.example.BankProject.Entity.User;
import com.example.BankProject.UserDTO.UserResponseDTO;

public class UserMapper {
	
	public static User toEntity(CreateUserDTO dto) {
		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setRole(dto.getRole());
		user.setActive(true);
		user.setCreatedAt(LocalDateTime.now());
		return user;
	}
	
	public static UserResponseDTO toDTO(User user) {
		UserResponseDTO dto = new UserResponseDTO();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setRole(user.getRole());
		dto.setActive(user.isActive());
		dto.setCreatedAt(user.getCreatedAt());
		return dto;
	}

}
