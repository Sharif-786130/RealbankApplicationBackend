package com.example.BankProject.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.BankProject.ENUM.Role;
import com.example.BankProject.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);
	
	long countByRole(Role role);
	
	List<User> findByRole(Role role);
	
	List<User> findByNameContainingIgnoreCaseAndRole(String name,Role role);
	
	List<User> findByActiveTrueAndRole(Role role);
	
	Optional<User> findByEmailAndActiveTrue(String email);
	
//	Optional<User> findByUsername(String username);

}
