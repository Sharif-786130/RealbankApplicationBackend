package com.example.BankProject.Security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.BankProject.Entity.User;
import com.example.BankProject.Repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepo;
	
	public CustomUserDetailsService(UserRepository userRepo) {
		this.userRepo =userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String email)
						throws UsernameNotFoundException {
		
		User user =userRepo.findByEmailAndActiveTrue(email)
				.orElseThrow(() ->
				         new UsernameNotFoundException("User are Not Found"));
		
//		return new CustomUserDetails(
//				user.getId(),
//				user.getEmail(),
//				user.getPassword(),
//				user.isActive(),
//				user.getBranchCode(),
//				List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
		
		return new CustomUserDetails(
				user,
				List.of(new SimpleGrantedAuthority(
						"ROLE_" + user.getRole().name())));
	}

}
